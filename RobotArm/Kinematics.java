	/**
	 * Kinematics
	 * Class for controlling a robotic arm, with 4 controllable axis (or 4 degrees of freedom).
	 * set_arm(double x, double y, double z, double grip_angle_d) takes x,y,z-coordinate and approach 
	 * angle and calculate into angle for base-, shoulder-, elbow-, and wrist-motor. 
	 * 
	 * @author Rasmus Lunding
	 * @version 2.1
	 * 
	 */
	public class Kinematics {
		
		private Kinematics(){
			
		}
        
		private static double length [] = {
                138, //ground to shoulder
                227, //shoulder to elbow
                153, //elbow to wrist
                120};//wrist to end of arm
		
		public static double[] goToPoint(double x, double y, double z, double grip_angle){
			double base_angle, shoulder_angle, elbow_angle, wrist_angle;
			double grip_angle_radians, y_wrist, z_wrist, a, b, a2, b2 , sw;
			//double base = 50, shoulder_elbow = 150, elbow_wrist = 150, gripper = 100;
			grip_angle_radians = Math.toRadians(grip_angle);
			
			base_angle = Math.toDegrees(Math.atan2(x, y));
			y = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
			y_wrist = y - Math.cos(grip_angle_radians) * length[3];
			z_wrist = z - Math.sin(grip_angle_radians) * length[3] - length[0];
			sw = Math.sqrt(Math.pow(y_wrist, 2) + Math.pow(z_wrist, 2));
			a = Math.pow(length[1], 2) + Math.pow(sw, 2) - Math.pow(length[2], 2);
			b = 2 * length[1] * sw;
			shoulder_angle = Math.toDegrees(Math.atan2(z_wrist, y_wrist) + Math.acos(a/b));
			a2 = Math.pow(length[1], 2) + Math.pow(length[2], 2) - Math.pow(sw, 2);
			b2 = 2 * length[1] * length[2];
			elbow_angle = Math.toDegrees(Math.acos(a2/b2))-180;
			wrist_angle = grip_angle - elbow_angle - shoulder_angle;
			
			double output[] = {base_angle, shoulder_angle, elbow_angle, wrist_angle};
			return output;
		}
		
		public static double[] goToPoint(Point p){
			return goToPoint(p.getX(), p.getY(), p.getZ(), p.getAngle());
		}
}