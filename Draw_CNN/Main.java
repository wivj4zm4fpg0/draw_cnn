package Draw_CNN;

import processing.core.*;

public class Main extends PApplet {

    private final int width_number = 4;
    private final int height_number = 4;
    private final int z_number = 4;

    private final int kernel_x = 3;
    private final int kernel_y = 3;
    private final int kernel_z = 2;

//    final int stride_x = 1;
//    final int stride_y = 1;
//    final int stride_z = 1;

    private int pixel_values[][][] = new int[z_number][height_number][width_number];
    private int weights[][][] = new int[kernel_z][kernel_y][kernel_x];

    private float box_width = 50;
    private float box_height = 50;
    private float space = 25;
    private float x = space * (this.z_number - 1);
    private float y = 0;
    private float matrix_space = 200;

    private int index = 0;

    private CNN cnn;

    @Override
    public void settings() {
        size((int) (x + box_width * pixel_values[0][0].length + space * (weights.length - 1) + matrix_space + box_width * weights[0][0].length + matrix_space + box_width * (width_number - kernel_x + 1) + space * (z_number - kernel_z)), (int) (y + this.space * (this.z_number - 1) + this.box_height * this.height_number));
    }

    @Override
    public void setup() {
        background(255);
        frameRate(1);
        textAlign(CENTER, CENTER);
        textSize(16);
//        videoExport = new VideoExport(this);
//        videoExport.startMovie();
        for (int i = 0; i < this.pixel_values.length; i++) {
            for (int j = 0; j < this.pixel_values[0].length; j++) {
                for (int k = 0; k < this.pixel_values[0][0].length; k++) {
                    this.pixel_values[i][j][k] = (int) random(10);
                }
            }
        }
        for (int i = 0; i < this.weights.length; i++) {
            for (int j = 0; j < this.weights[0].length; j++) {
                for (int k = 0; k < this.weights[0][0].length; k++) {
                    this.weights[i][j][k] = (int) random(10);
                }
            }
        }
        this.cnn = new CNN(this.x, this.y, this.pixel_values, this.weights, this.space, this.box_width, this.box_height, this, matrix_space);
    }

    @Override
    public void draw() {
        background(255);
        this.cnn.index_update(this.index);
        this.cnn.draw_3DCNN();
        strokeWeight(3);
        this.cnn.draw_multi_sign();
        strokeWeight(1);
        this.cnn.draw_weight_3DCNN();
        strokeWeight(3);
        this.cnn.draw_equal_sign();
        strokeWeight(1);
        this.cnn.draw_out_3DCNN();
        this.index++;
        saveFrame("frames/image_#####.jpg");
        int max_index = (this.width_number - this.kernel_x + 1) * (this.height_number - this.kernel_y + 1) * (this.z_number - this.kernel_z + 1);
        if (this.index == max_index) {
            exit();
//            index = 0;
//            cnn.reset();
        }
    }

    public static void main(String[] args) {
        PApplet.main("Draw_CNN.Main");
    }
}