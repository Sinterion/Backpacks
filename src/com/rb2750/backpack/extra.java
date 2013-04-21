package com.rb2750.backpack;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

public class extra
{
	public ItemStack setSkullSkin(ItemStack item, String skinUsername)
	{
		SkullMeta sm = (SkullMeta) item.getItemMeta();
		sm.setOwner(skinUsername);
		item.setItemMeta(sm);
		
		return item;
	}
	
	public static String getItemName(ItemStack item)
	{
		return item.getItemMeta().getDisplayName();
	}
	
	public static ItemStack renameItem(ItemStack item, String newName)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(newName);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack addLore(ItemStack item, String lore)
	{
		ItemStack result = item;
		
		ItemMeta meta = item.getItemMeta();
		
		ArrayList<String> l = new ArrayList<String>();
		if (!meta.hasLore())
		{
			l.add(lore);
		}
		else
		{
			l.addAll(meta.getLore());
			l.add(lore);
		}
		meta.setLore(l);
		
		result.setItemMeta(meta);
		
		return result;
	}
	
	public int random(int min, int max)
	{
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
	public static ItemStack setLore(ItemStack item, String lore, int line)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = new ArrayList<String>();
		itemLore.add(lore);
		meta.setLore(itemLore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack removeLore(ItemStack item, int index)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.getLore();
		itemLore.remove(index);
		meta.setLore(itemLore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static String getLore(ItemStack item, int index)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.getLore();
		
		if (itemLore != null && itemLore.get(index) != null)
			return itemLore.get(index);
		else
			return null;
	}
	
	public static List<String> getLore(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		
		if (meta == null)
			return null;
		
		return meta.getLore();
	}
	
	public static int Random(int Min, int Max)
	{
		return Min + (int) (Math.random() * ((Max - Min) + 1));
	}
	
	public static ItemStack setLeatherColour(ItemStack item, int red, int green, int blue)
	{
		LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
		lam.setColor(Color.fromRGB(red, green, blue));
		item.setItemMeta(lam);
		return item;
	}
	
	public static void clearInv(Player player)
	{
		for (PotionEffect effect : player.getActivePotionEffects())
		{
			player.removePotionEffect(effect.getType());
		}
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		player.getInventory().clear();
	}
}
