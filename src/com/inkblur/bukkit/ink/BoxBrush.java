package com.inkblur.bukkit.ink;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.inkblur.bukkit.claims.InkblurClaims;

public class BoxBrush extends InkBrush {
	private Block first = null;
	private boolean hollow;

	@Override
	public String getName() {
		return "Box Brush";
	}

	@Override
	public void inkRightClick(Block arg) {
		if (first == null) {
			first = arg;
		} else {
			execute(new BoxExecutor(player.getName(), first, arg, material,
					replace, hollow));
			first = null;
		}
		updateHUD();
	}

	@Override
	protected String[] inkHUD() {
		String[] strs = {
				"Pt A: "
						+ (first != null ? ChatColor.GREEN + "Set"
								: ChatColor.RED + "null"),
				ChatColor.UNDERLINE + "hollow",
				hollow ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No" };
		return strs;
	}

	@Override
	public void inkLeftClick(Block arg) {
	}

	@Override
	protected void inkCommand(String[] arg) {
		if (arg.length > 0)
			if (arg[0].equalsIgnoreCase("hollow")
					|| arg[0].equalsIgnoreCase("h"))
				hollow = !hollow;
	}
}

class BoxExecutor implements Runnable {
	private final int l1x, l1y, l1z, l2x, l2y, l2z;
	private final World world;
	private final String playerName;
	private final Material mat;
	private final Material replace;
	private final boolean hollow;
	private Set<Block> blocks = new HashSet<Block>();

	public BoxExecutor(String player, Block l1, Block l2, Material mat,
			Material replace, boolean hollow) {
		playerName = player;
		if (l1.getX() > l2.getX()) {
			l2x = l1.getX();
			l1x = l2.getX();
		} else {
			l1x = l1.getX();
			l2x = l2.getX();
		}
		if (l1.getY() > l2.getY()) {
			l2y = l1.getY();
			l1y = l2.getY();
		} else {
			l1y = l1.getY();
			l2y = l2.getY();
		}
		if (l1.getZ() > l2.getZ()) {
			l2z = l1.getZ();
			l1z = l2.getZ();
		} else {
			l1z = l1.getZ();
			l2z = l2.getZ();
		}
		world = l1.getWorld();
		this.mat = mat;
		this.replace = replace;
		this.hollow = hollow;
	}

	void add(int i, int j, int k) {
		Block b = world.getBlockAt(i, j, k);
		if (replace == null || b.getType() == replace)
			blocks.add(b);
	}

	@Override
	public void run() {
		if (hollow) {
			if (!InkBrush.checkAllowance(2 * ((l2y - l1y + l2x - l1x)
					* (l2z - l1z) + (l2y - l1y) * (l2x - l1x))))
				return;
			for (int j = l1y; j <= l2y; j++)
				for (int k = l1z; k <= l2z; k++) {
					add(l1x, j, k);
					add(l2x, j, k);
				}
			for (int i = l1x; i <= l2x; i++) {
				for (int k = l1z; k <= l2z; k++) {
					add(i, l1y, k);
					add(i, l2y, k);
				}
				for (int j = l1y; j <= l2y; j++) {
					add(i, j, l1z);
					add(i, j, l2z);
				}
			}
		} else {
			if (!InkBrush.checkAllowance((l2x - l1x) * (l2y - l1y)
					* (l2z - l1z)))
				return;
			for (int i = l1x; i <= l2x; i++)
				for (int j = l1y; j <= l2y; j++)
					for (int k = l1z; k <= l2z; k++)
						add(i, j, k);
		}
		if (Ink.useClaims())
			blocks = InkblurClaims.canBuildFilter(playerName, world.getName(),
					blocks);
		for (Block b : blocks) {
			b.setType(mat);
		}
	}
}
