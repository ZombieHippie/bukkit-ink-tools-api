package com.inkblur.bukkit.ink;

import java.lang.reflect.Array;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.inkblur.tools.Tool;

public abstract class InkBrush extends Tool {
	protected Material replace = null;
	protected Material material = Material.AIR;
	private boolean eyedropReplace = false;
	private boolean eyedropMaterial = false;
	
	public static boolean checkAllowance(int n){
		return n <= 3000;
	}
	
	public void onSwitch(int index) {
		
	}

	@Override
	public String[] getHUD() {
		String[] hud = {
				ChatColor.UNDERLINE + "material:",
				(eyedropMaterial ? "???" : material.toString()),
				ChatColor.UNDERLINE + "replace:",
				(eyedropReplace ? "???" : (replace == null ? "all" : replace
						.toString())), };
		return concatenate(hud, inkHUD());
	}

	protected abstract String[] inkHUD();

	protected abstract void inkCommand(String[] arg);

	public <T> T[] concatenate(T[] A, T[] B) {
		int aLen = A.length;
		int bLen = B.length;

		@SuppressWarnings("unchecked")
		T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen
				+ bLen);
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);

		return C;
	}

	@Override
	public void onCommand(String[] arg) {
		if (arg.length == 1 || arg.length == 2) {
			if (arg[0].equalsIgnoreCase("replace")
					|| arg[0].equalsIgnoreCase("r")) {
				if (arg.length == 2) {
					if (arg[1].equals("?")) {
						eyedropReplace = true;
					} else if (arg[1].equalsIgnoreCase("all")) {
						setReplace(null);
					} else {
						setReplace(Material.matchMaterial(arg[1]));
					}
				} else {
					eyedropReplace = true;
				}
			} else if (arg[0].equalsIgnoreCase("material")
					|| arg[0].equalsIgnoreCase("m")) {
				if (arg.length == 2) {
					if (arg[1].equals("?")) {
						eyedropMaterial = true;
					} else {
						this.setMaterial(Material.matchMaterial(arg[1]));
					}
				} else {
					eyedropMaterial = true;
				}
			} else if (arg[0].equalsIgnoreCase("right")){
				this.inkRightClick(player.getLocation().getBlock());
			} else if (arg[0].equalsIgnoreCase("left")){
				this.inkLeftClick(player.getLocation().getBlock());
			}
				else {
				inkCommand(arg);
			}
		}
		updateHUD();
	}

	private void setMaterial(Material mat) {
		if (mat == null)
			mat = Material.AIR;
		material = mat;
		eyedropMaterial = false;
	}

	private void setReplace(Material mat) {
		replace = mat;
		eyedropReplace = false;
	}

	public abstract void inkLeftClick(Block arg);

	@Override
	public boolean onLeftClick(Block arg0) {
		if (player.isSneaking()) {
			setMaterial(arg0.getType());
			updateHUD();
		} else {
			inkLeftClick(arg0);
		}
		return true;
	}

	public abstract void inkRightClick(Block arg);

	@Override
	public boolean onRightClick(Block arg0) {
		if (player.isSneaking()) {
			setReplace(arg0.getType());
		} else if (!eyedropReplace && !eyedropMaterial) {
			inkRightClick(arg0);
		} else {
			if (eyedropReplace) {
				setReplace(arg0.getType());
				eyedropReplace = false;
			}
			if (eyedropMaterial) {
				setMaterial(arg0.getType());
				eyedropMaterial = false;
			}
		}
		updateHUD();
		return true;
	}
}
