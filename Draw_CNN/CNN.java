package Draw_CNN;

import processing.core.PApplet;

import static processing.core.PApplet.str;

class CNN {
    private float x;
    private float y;
    private int pixel_values[][][];
    private int weights[][][];
    private int outs[][][];
    private float space;
    private float box_width;
    private float box_height;
    private float weight_x;
    private float weight_y;
    private int index_x;
    private int index_y;
    private int index_z;
    private float out_x;
    private float out_y;
    private int index;
    private PApplet pApplet;
    private float matrix_space_width[] = new float[2];
    private float matrix_space_height;
    private float matrix_space;

    CNN(float x, float y, int pixel_values[][][], int weights[][][], float space, float box_width, float box_height, PApplet pApplet, float matrix_space) {
        this.x = x;
        this.y = y;
        this.pixel_values = pixel_values;
        this.weights = weights;
        this.outs = new int[pixel_values.length - weights.length + 1][pixel_values[0].length - weights[0].length + 1][pixel_values[0][0].length - weights[0][0].length + 1];
        this.space = space;
        this.box_width = box_width;
        this.box_height = box_height;
        this.weight_x = x + box_width * pixel_values[0][0].length + space * (weights.length - 1) + matrix_space;
        this.weight_y = y + ((pixel_values.length * space + pixel_values[0].length * box_height) - (weights.length * space + weights[0].length * box_height)) / 2;
        this.out_x = this.weight_x + box_width * weights[0][0].length + matrix_space;
        this.out_y = y + ((pixel_values.length * space + pixel_values[0].length * box_height) - (this.outs.length * space + this.outs[0].length * box_height)) / 2 + (this.outs.length - 1) * space;
        this.pApplet = pApplet;
        this.matrix_space_width[0] = this.pixel_values[0].length * this.box_height + (this.pixel_values.length - 1) * this.space;
        this.matrix_space_width[1] = this.weight_x + box_width * weights[0][0].length;
        this.matrix_space_height = x + box_width * pixel_values[0][0].length + space * (weights.length - 1);
        this.matrix_space = matrix_space;
    }

    void index_update(int index) {
        this.index_x = index % this.outs[0][0].length;
        this.index_y = index % (this.outs[0][0].length * this.outs[0].length) / this.outs[0][0].length;
        this.index_z = index / (this.outs[0].length * this.outs[0][0].length);
        this.index = index;
    }

    void reset() {
        for (int i = 0; i < this.outs.length; i++) {
            for (int j = 0; j < this.outs.length; j++) {
                for (int k = 0; k < this.outs.length; k++) {
                    this.outs[i][j][k] = 0;
                }
            }
        }
    }

    private void draw_2DCNN(float x, float y, int index_z, int index_z2) {

        int out = 0;

        pApplet.fill(255);
        for (int i = 0; i < this.pixel_values[0].length; i++) {
            for (int j = 0; j < this.pixel_values[0][0].length; j++) {
                pApplet.rect(x + this.box_width * j, y + this.box_height * i, this.box_width, this.box_height);
            }
        }

        pApplet.fill(0, 255, 255);
        for (int i = 0; i < this.weights[0].length; i++) {
            for (int j = 0; j < this.weights[0][0].length; j++) {
                pApplet.rect(x + this.box_width * (this.index_x + j), y + this.box_height * (this.index_y + i), this.box_width, this.box_height);
                out += this.pixel_values[index_z][this.index_y + i][this.index_x + j] * this.weights[index_z2][i][j];
            }
        }

        pApplet.fill(0);
        for (int i = 0; i < this.pixel_values[0].length; i++) {
            for (int j = 0; j < this.pixel_values[0][0].length; j++) {
                pApplet.text(str(pixel_values[index_z][i][j]), x + this.box_width * j + this.box_width / 2, y + this.box_height * i + this.box_height / 2);
            }
        }

        this.outs[this.index_z][this.index_y][this.index_x] += out;
    }

    private void draw_2DCNN(float x, float y, int index_z, int values_[][][]) {

        for (int i = 0; i < values_[0].length; i++) {
            for (int j = 0; j < values_[0][0].length; j++) {
                pApplet.fill(255);
                pApplet.rect(x + this.box_width * j, y + this.box_height * i, this.box_width, this.box_height);

                pApplet.fill(0);
                pApplet.text(str(values_[index_z][i][j]), x + this.box_width * j + this.box_width / 2, y + this.box_height * i + this.box_height / 2);
            }
        }
    }

    void draw_3DCNN() {

        int i = 0;

        for (int j = this.pixel_values.length - 1; j >= index_z + this.weights.length; j--) {
            this.draw_2DCNN(this.x - i * this.space, this.y + i * this.space, j, this.pixel_values);
            i++;
        }

        for (int j = index_z + this.weights.length - 1, k = this.weights.length - 1; j >= index_z; j--, k--) {
            this.draw_2DCNN(this.x - i * this.space, this.y + i * this.space, j, k);
            i++;
        }

        for (int j = index_z - 1; j >= 0; j--) {
            this.draw_2DCNN(this.x - i * this.space, this.y + i * this.space, j, this.pixel_values);
            i++;
        }
    }

    void draw_weight_3DCNN() {

        for (int i = this.weights.length - 1, j = 0; i >= 0; i--, j++) {
            this.draw_2DCNN(this.weight_x - j * this.space, this.weight_y + j * this.space, i, this.weights);
        }
    }

    private void draw_out_2DCNN(float x, float y) {

        pApplet.fill(255);
        for (int i = 0; i < this.index % (this.outs[0].length * this.outs[0][0].length); i++) {
            int i1 = i % (this.outs[0][0].length * this.outs[0].length) / this.outs[0][0].length;
            pApplet.rect(x + this.box_width * (i % this.outs[0][0].length), y + this.box_height * i1, this.box_width, this.box_height);
        }

        pApplet.fill(0, 255, 255);
        pApplet.rect(x + this.box_width * this.index_x, y + this.box_height * this.index_y, this.box_width, this.box_height);

        pApplet.fill(0);
        for (int i = 0; i <= this.index % (this.outs[0].length * this.outs[0][0].length); i++) {
            int xi = i % this.outs[0][0].length;
            int yi = i % (this.outs[0][0].length * this.outs[0].length) / this.outs[0][0].length;
            pApplet.text(str(this.outs[this.index_z][yi][xi]), x + this.box_width * xi + this.box_width / 2, y + this.box_height * yi + this.box_height / 2);
        }
    }

    void draw_out_3DCNN() {
        draw_out_2DCNN(this.out_x + this.index_z * this.space, this.out_y - this.index_z * this.space);
        for (int i = this.index_z - 1; i >= 0; i--) {
            draw_2DCNN(this.out_x + i * this.space, this.out_y - i * this.space, i, this.outs);
        }
    }

    void draw_multi_sign() {
        int space = 30;
        pApplet.line(this.matrix_space_width[0] + matrix_space / 2 - space, this.matrix_space_height / 2 - space, this.matrix_space_width[0] + matrix_space / 2 + space, matrix_space_height / 2 + space);
        pApplet.line(this.matrix_space_width[0] + matrix_space / 2 - space, this.matrix_space_height / 2 + space, this.matrix_space_width[0] + matrix_space / 2 + space, matrix_space_height / 2 - space);
    }

    void draw_equal_sign() {
        int space_x = 20;
        int space_y = 8;
        pApplet.line(this.matrix_space_width[1] + matrix_space / 2 - space_x, this.matrix_space_height / 2 - space_y, this.matrix_space_width[1] + matrix_space / 2 + space_x, matrix_space_height / 2 - space_y);
        pApplet.line(this.matrix_space_width[1] + matrix_space / 2 - space_x, this.matrix_space_height / 2 + space_y, this.matrix_space_width[1] + matrix_space / 2 + space_x, matrix_space_height / 2 + space_y);
    }
}
