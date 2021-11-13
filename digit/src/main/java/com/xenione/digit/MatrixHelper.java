package com.xenione.digit;


import ohos.agp.render.Canvas;
import ohos.agp.render.ThreeDimView;
import ohos.agp.utils.Matrix;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class MatrixHelper {

//    public static final Camera camera = new Camera();
    public static final Canvas canvas = new Canvas();
    public static final ThreeDimView threeDimView = new ThreeDimView();
    /**
     * Matrix with 180 degrees x rotation defined
     */
    public static final Matrix MIRROR_X = new Matrix();
    static {
        MatrixHelper.rotateX(MIRROR_X, 180);
    }

    /**
     * Matrix with 0 degrees x rotation defined
     */
    public static final Matrix ROTATE_X_0 = new Matrix();

    static {
        MatrixHelper.rotateX(ROTATE_X_0, 0);
    }

    /**
     * Matrix with 90 degrees x rotation defined
     */
    public static final Matrix ROTATE_X_90 = new Matrix();

    static {
        MatrixHelper.rotateX(ROTATE_X_90, 90);
    }

    public static void mirrorX(Matrix matrix) {
        rotateX(matrix, 180);
    }

    public static void rotateX(Matrix matrix, int alpha) {
        synchronized (canvas) {
//            camera.save();
//            camera.rotateX(alpha);
//            camera.getMatrix(matrix);
//            camera.restore();
            canvas.save();
            threeDimView.rotateX(alpha);
            threeDimView.applyToCanvas(canvas);
//            canvas.rotate(alpha);
            canvas.getMatrix(matrix);
            canvas.restore();
        }
    }

    public static void rotateZ(Matrix matrix, int alpha) {
        synchronized (canvas) {
//            camera.save();
//            camera.rotateZ(alpha);
//            camera.getMatrix(matrix);
//            camera.restore();
            canvas.save();
            threeDimView.rotateZ(alpha);
            threeDimView.applyToCanvas(canvas);
//            canvas.rotate(alpha);
            canvas.getMatrix(matrix);
            canvas.restore();
        }
    }

    public static void translate(Matrix matrix, float dx, float dy, float dz) {
        synchronized (canvas) {
//            camera.save();
//            camera.translate(dx, dy, dz);
//            camera.getMatrix(matrix);
//            camera.restore();
            canvas.save();
            //camera.translate(dx, dy, dz);
            canvas.translate(dx, dy);
//            threeDimView.rotateZ(dz);
//            threeDimView.applyToCanvas(canvas);
            canvas.getMatrix(matrix);
            canvas.restore();
        }
    }

    public static void translateY(Matrix matrix, float dy) {
        synchronized (canvas) {
//            camera.save();
//            camera.translate(0, dy, 0);
//            camera.getMatrix(matrix);
//            camera.restore();
            canvas.save();
            canvas.translate(0, dy);
            canvas.getMatrix(matrix);
            canvas.restore();
        }
    }
}
