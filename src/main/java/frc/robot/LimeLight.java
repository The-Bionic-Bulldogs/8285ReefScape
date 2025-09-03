package frc.robot;

    import edu.wpi.first.networktables.NetworkTable;
    import edu.wpi.first.networktables.NetworkTableInstance;
    
    public class LimeLight {
        private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    
        public boolean hasValidTarget() {
            double tv = table.getEntry("tv").getDouble(0.0);
            return tv == 1.0;
        }
        
    
        public double getX() {
            return table.getEntry("tx").getDouble(0.0);
        }
    
        public double getY() {
            return table.getEntry("ty").getDouble(0.0);
        }
    
        public double getArea() {
            return table.getEntry("ta").getDouble(0.0);
        }
    
        public void setPipeline(int pipeline) {
            table.getEntry("pipeline").setNumber(pipeline);
        }
    }
    
