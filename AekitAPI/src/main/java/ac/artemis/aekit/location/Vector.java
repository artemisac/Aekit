package ac.artemis.aekit.location;

public interface Vector {
    double getX();
    double getY();
    double getZ();

    Vector add(double x, double y, double z);
    Vector subtract(double x, double y, double z);
    Vector clone();
}
