package com.inkblur.bukkit.ink;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.inkblur.bukkit.claims.InkblurClaims;

public class SniperBrush extends InkBrush {
	@Override
	public String[] getHUD() {
		return ("Sniper Brush").split("\n");
	}

	@Override
	public String getName() {
		return "Sniper";
	}
	@Override
	protected String[] inkHUD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void inkRightClick(Block arg) {
		Material mat = player.getItemInHand() == null?
				Material.AIR:player.getItemInHand().getType();
		if(mat.isBlock() || mat == Material.AIR)
			if(Ink.useClaims()){
				if(InkblurClaims.canBuild(player.getName(), arg)){
					arg.setType(mat);
				}
			} else {
				arg.setType(mat);
			}
	}
	@Override
	public void inkLeftClick(Block arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void inkCommand(String[] arg) {
		// TODO Auto-generated method stub
		
	}
}
