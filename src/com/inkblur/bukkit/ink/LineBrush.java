package com.inkblur.bukkit.ink;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.inkblur.bukkit.claims.InkblurClaims;

public class LineBrush extends InkBrush {
	Location rightClicked = null;
	boolean continues = false;

	@Override
	public String getName() {
		return "Line Brush";
	}

	@Override
	public void onSwitch(int arg0) {
	}

	@Override
	protected String[] inkHUD() {
		String[] hud = {
				"Pt A: "
						+ (rightClicked == null ? ChatColor.RED + "null"
								: ChatColor.GREEN + "Set"),
				ChatColor.UNDERLINE+"continues",
				continues?ChatColor.GREEN+"yes":ChatColor.RED+"no"
		};
		return hud;
	}

	@Override
	public void inkRightClick(Block arg) {
		if (arg != null && !arg.isEmpty()) {
			if(rightClicked == null){
				rightClicked = arg.getLocation();
			} else {
				execute(new LineExecutor(player.getName(), arg.getLocation(), rightClicked, material, replace));
				rightClicked = continues?arg.getLocation():null;
			}
		}
		updateHUD();
	}

	@Override
	protected void inkCommand(String[] args) {
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("reset")){
				this.rightClicked = null;
			} else if(args[0].equalsIgnoreCase("continues")
					|| args[0].equalsIgnoreCase("c")){
				this.continues = !continues; 
			} else {
				return;
			}
			updateHUD();
		}
		
	}

	@Override
	public void inkLeftClick(Block arg) {
		// TODO Auto-generated method stub
		
	}
}

class LineExecutor implements Runnable {
	private final int l1x, l1y, l1z, ldx, ldy, ldz;
	private final World world;
	private final String playerName;
	private final Material mat;
	private final Material replace;

	public LineExecutor(String player, Location l1, Location l2, Material mat, Material replace) {
		playerName = player;
		l1x = l1.getBlockX();
		l1y = l1.getBlockY();
		l1z = l1.getBlockZ();
		ldx = l2.getBlockX() - l1x;
		ldy = l2.getBlockY() - l1y;
		ldz = l2.getBlockZ() - l1z;
		world = l1.getWorld();
		this.mat = mat;
		this.replace = replace;
	}

	@Override
	public void run() {
		double dt = 1 / Math.sqrt((ldx * ldx) + (ldy * ldy) + (ldz * ldz));
		if(Ink.useClaims()){
			Set<Block> blocks = new HashSet<Block>();
			for (double t = 0; t <= 1; t += dt) {
				Block b =world.getBlockAt(l1x + (int) Math.round(ldx * t),
						l1y + (int) Math.round(ldy * t),
						l1z + (int) Math.round(ldz * t));
				if(replace == null || b.getType() == replace)
					blocks.add(b);
			}
			blocks = InkblurClaims.canBuildFilter(playerName, world.getName(), blocks);
			for(Block b : blocks){
				b.setType(mat);
			}
		} else {
			for (double t = 0; t <= 1; t += dt) {
				world.getBlockAt(l1x + (int) Math.round(ldx * t),
						l1y + (int) Math.round(ldy * t),
						l1z + (int) Math.round(ldz * t)).setType(mat);
			}
		}
	}
}
