// Sam Coward - 2015

package com.DramaCow.maths;

public class Collision {

	// Prevent instantiation
	private Collision() {}
	
	public static Contact AABBvsAABB(AABB a, AABB b) {
		Vector2D mdHalfExtents  = Vector2D.add( a.halfExtents, b.halfExtents );
		Vector2D mdCenter = new Vector2D(b.position);

		Vector2D point = new Vector2D(a.position);

		// Difference vector between object centers
		Vector2D delta = Vector2D.sub( mdCenter, point );

		// Get distance away from obstacle
		Vector2D difference = Vector2D.abs(delta).sub( mdHalfExtents );
		float distance = difference.maxComponent();
		
		// Edit ratio of the delta (necessary for non-square characters)
		delta.x *= mdHalfExtents.y / mdHalfExtents.x;

		return new Contact(Vector2D.majorAxis(delta).neg(), distance, point);
	}

	public static CollisionObject collisionResponse(CollisionObject object, Contact contact, float dt) {
		Vector2D velocity = new Vector2D(object.velocity);
		Vector2D posCorrect = new Vector2D(object.posCorrect);

		// Computed seperately to prevent objects pinging apart
		float seperation = contact.distance > 0.0f ? contact.distance : 0.0f;
		float penetration = contact.distance < 0.0f ? contact.distance : 0.0f;

		// Relative normal velocity required for an exact stop at surface
		float nv = Vector2D.dot(velocity, contact.normal) + seperation/dt;

		// Accumulate penetration correction
		if (penetration != 0.0f) System.out.println(penetration);
		posCorrect.sub( Vector2D.scalar(penetration/dt, contact.normal) );

		if (nv < 0) {
			velocity.sub( Vector2D.scalar(nv, contact.normal) );

			if 		(contact.normal.y < 0) object.north = true; 
			else if	(contact.normal.y > 0) object.south = true; 

			if 		(contact.normal.x < 0) object.east = true; 
			else if	(contact.normal.x > 0) object.west = true;			
		}	

		return new CollisionObject(object.box, velocity, object.acceleration, posCorrect);
	}
}
