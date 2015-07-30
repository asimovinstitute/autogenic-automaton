
package aa.model;

import java.util.*;

import aa.AAUtil;

public class Particle2DArrayList extends ArrayList<Particle2D> {  
	
	public Particle2DArrayList getAndRemoveParticlesAt(int x, int y) {		
		Particle2DArrayList particlesAtXY = new Particle2DArrayList();
		for(int i=(this.size()-1); i>=0; i--) {
			if(this.get(i).getX() == x && this.get(i).getY() == y) {
				particlesAtXY.add(this.get(i));
				this.remove(i);
			}
		}
		return particlesAtXY;		
	}

	public Particle2DArrayList getAndRemoveBasicParticlesAt(int x, int y) {		
		Particle2DArrayList particlesAtXY = new Particle2DArrayList();
		for(int i=(this.size()-1); i>=0; i--) {
			if(this.get(i).getX() == x && this.get(i).getY() == y && !(this.get(i) instanceof Capsule2D)) {
				particlesAtXY.add(this.get(i));
				this.remove(i);
			}
		}
		return particlesAtXY;		
	}	
	
	public Particle2DArrayList getAndRemoveBasicParticlesOfTypeAt(AAUtil.type type, int x, int y) {
		Particle2DArrayList particlesAtXY = new Particle2DArrayList();
		for(int i=(this.size()-1); i>=0; i--) {
			if(this.get(i).getX() == x && this.get(i).getY() == y && !(this.get(i) instanceof Capsule2D) && ((BasicParticle2D)this.get(i)).isOfType(AAUtil.type.C)) {
				particlesAtXY.add(this.get(i));
				this.remove(i);
			}
		}
		return particlesAtXY;		
	}
	
	public int getTotalCatalysts(int x, int y) {
		int sum = 0;
		for(Particle2D p : this) {
			if(p.getX() == x && p.getY() == y && p instanceof BasicParticle2D && (((BasicParticle2D)p).isOfType(AAUtil.type.C) || ((BasicParticle2D)p).isOfType(AAUtil.type.F))) {
				sum++;
			}
		}
		return sum;
	}
	
	public BasicParticle2D getAndRemoveRandomCatalyst(int x, int y, int totalCatalysts) {
		int picked = AAUtil.RANDOM.nextInt(totalCatalysts);
		int index = 0;
		for(Particle2D p: this) {
			if(p.getX() == x && p.getY() == y && p instanceof BasicParticle2D && (((BasicParticle2D)p).isOfType(AAUtil.type.C) || ((BasicParticle2D)p).isOfType(AAUtil.type.F))) {
				if(index == picked) {
					this.remove(p);
					return (BasicParticle2D)p;
				}
				else {
					index++;
				}			
			}
		}
		System.out.println("Error in ParticleList");
		return (BasicParticle2D)this.get(0);
	}
}
