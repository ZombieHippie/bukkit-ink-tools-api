package com.inkblur.bukkit.ink;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BallBrush extends InkBrush {
	private static boolean[][][] hollowvoxels;
	private float radius = 5;
	@Override
	protected String[] inkHUD() {
		String[] hud = {
				ChatColor.UNDERLINE.toString()+"radius",
				ChatColor.YELLOW.toString()+radius
		};
		return hud;
	}

	@Override
	protected void inkCommand(String[] arg) {
		if(arg.length == 2){
			if(arg[0].equalsIgnoreCase("radius")
					|| arg[0].equalsIgnoreCase("r")){
				try{
					float newR = Float.parseFloat(arg[1]);
					radius = newR;
				} catch (NumberFormatException e){
					player.sendMessage(ChatColor.RED+"Could not parse as float:"+arg[1]);
				}
			}
		}
	}

	@Override
	public void inkLeftClick(Block arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inkRightClick(Block arg) {
		execute(new BallExecutor(arg,radius,replace, material));
	}

	@Override
	public String getName() {
		return "Ball Brush";
	}
}
class BallExecutor implements Runnable {

	private final Material material;
	private final Material replace;
	private final float radius;
	private final float diameter;
	private final float radius2;
	private final World world;
	private final int x, y, z;
	private Set<Block> blocks = new HashSet<Block>();

	public BallExecutor(Block arg, float radius, Material replace,
			Material material) {
		this.material = material;
		this.radius = radius;
		this.diameter = radius+radius;
		this.radius2 = radius*radius;
		this.world = arg.getWorld();
		this.replace = replace;
		this.x = arg.getX() - (int) radius;
		this.y = arg.getY() - (int) radius;
		this.z = arg.getZ() - (int) radius;
	}
	private float FValue(float x, float y, float z){
		return x*x + y*y + z*z - radius2;
	}
	private void add(int x, int y, int z){
		
	}
	@Override
	public void run() {
		for(int i = 0; i < diameter; i++){
			
		}
	}
	
}
