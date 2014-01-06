package com.inkblur.bukkit.ink;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.inkblur.tools.ToolsApi;

public class Ink extends JavaPlugin{
	private static boolean useInkblurClaims = false;
	@Override
	public void onEnable(){
		ToolsApi.registerItem(SniperBrush.class, Material.ARROW, "sniper");
		ToolsApi.registerItem(LineBrush.class, Material.STRING, "line");
		ToolsApi.registerItem(BoxBrush.class, Material.BOOK, "box");
		useInkblurClaims = Bukkit.getPluginManager().isPluginEnabled("blur-claims");			
	}
	@Override
	public void onDisable(){
		
	}
	public static boolean useClaims(){
		return useInkblurClaims;
	}
}
