package durakonline.sk.durakonline.other;

public class PointPP 
{
    public static final int X_ID = 0;
    public static final int Y_ID = 1;
    
    private float x;
    private float y;
    
    public PointPP(float x, float y) {
            this.x=x;
            this.y=y;
    }

    public static float distance(PointPP p1, PointPP p2) {
            return (float) Math.sqrt(Math.pow(p1.getx() - p2.getx(), 2) + Math.pow(p1.gety() - p2.gety(), 2));
    }
    
    public float getx() {
            return x;
    }
    
    public float gety() {
            return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public float getCoord(int id) {
            if (id==X_ID)
                    return x;
            return y;
    }
}
