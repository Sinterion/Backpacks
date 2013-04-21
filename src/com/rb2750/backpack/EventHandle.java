package com.rb2750.backpack;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventHandle implements Listener
{
	Backpack plugin;
	
	public EventHandle(Backpack plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			if (plugin.getConfig().getBoolean(plugin.deathDrop))
			{
				String split = null;
				if (plugin.getConfig().getBoolean(plugin.perWorld))
				{
					split = plugin.getConfig().getString("Players." + player.getName() + ".InventoryWorld." + player.getWorld().getName());
					plugin.getConfig().set("Players." + player.getName() + ".InventoryWorld." + player.getWorld().getName(), null);
				}
				else
				{
					split = plugin.getConfig().getString("Players." + player.getName() + ".Inventory");
					plugin.getConfig().set("Players." + player.getName() + ".Inventory", null);
				}
				
				if (split != null)
				{
					if (split.length() != 0)
					{
						String[] parts = split.split(",");
						for (String part : parts)
						{
							String[] data = null;
							int id = 0;
							short damage = 0;
							int amount = 0;
							ItemStack item = null;
							
							try
							{
								part = part.trim();
								data = part.split(":");
								id = Integer.parseInt(data[1]);
								damage = Short.parseShort(data[2]);
								amount = Integer.parseInt(data[3]);
								item = new ItemStack(id, amount, damage);
								if (data.length > 4)
								{
									if (!data[4].trim().equalsIgnoreCase("0"))
									{
										String[] enchantments = part.split(":")[4].trim().split("\\.");
										
										for (String enchant : enchantments)
										{
											item.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(enchant.split(";")[0])), Integer.parseInt(enchant.split(";")[1]));
										}
									}
								}
								
								event.getDrops().add(item);
							}
							catch (Exception e)
							{
								try
								{
									id = Integer.parseInt(part.split(";")[0].split(":")[0]);
									damage = Short.parseShort(part.split(";")[0].split(":")[1]);
									amount = Integer.parseInt(part.split(";")[1]);
									item = new ItemStack(id, amount, damage);
									event.getDrops().add(item);
								}
								catch (Exception ex)
								{
									try
									{
										part = part.trim();
										data = part.split(":");
										id = Integer.parseInt(data[1]);
										damage = Short.parseShort(data[2]);
										amount = Integer.parseInt(data[3]);
										item = new ItemStack(id, amount, damage);
										if (data.length > 4)
											if (data[4] != "''")
												extra.renameItem(item, data[4].replace("'", ""));
										
										if (data.length > 5)
										{
											if (!data[5].trim().equalsIgnoreCase("0"))
											{
												String[] enchantments = part.split(":")[4].trim().split("\\.");
												
												for (String enchant : enchantments)
												{
													item.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(enchant.split(";")[0])), Integer.parseInt(enchant.split(";")[1]));
												}
											}
										}
										
										event.getDrops().add(item);
									}
									catch (Exception exc)
									{
										try
										{
											part = part.trim();
											data = part.split(":");
											id = Integer.parseInt(data[1]);
											damage = Short.parseShort(data[2]);
											amount = Integer.parseInt(data[3]);
											item = new ItemStack(id, amount, damage);
											if (data.length > 4)
												if (data[4] != "''")
												{
													String[] lores = data[4].split("\\n");
													if (lores.length > 0)
													{
														extra.renameItem(item, lores[0]);
														for (String lore : lores)
															if (lores[0] != lore)
																extra.addLore(item, lore);
													}
													else
													{
														extra.renameItem(item, data[4].replace("'", ""));
													}
												}
											
											if (data.length > 5)
											{
												if (data[5] != "''")
												{
													String[] lores = data[5].split("‖");
													if (lores.length > 0)
														for (String lore : lores)
															extra.addLore(item, ChatColor.WHITE + lore);
												}
											}
											
											if (data.length > 6)
											{
												if (!data[6].trim().equalsIgnoreCase("0"))
												{
													String[] enchantments = data[6].trim().split("\\.");
													
													for (String enchant : enchantments)
													{
														item.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(enchant.split(";")[0])), Integer.parseInt(enchant.split(";")[1]));
													}
												}
											}
											
											event.getDrops().add(item);
										}
										catch (Exception exce)
										{
											exce.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
				plugin.saveConfig();
				
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event)
	{
		int size = event.getInventory().getSize() / 9;
		if (size > 0 && size < 7)
		{
			String title = plugin.getConfig().getString("Title." + size).replace("&", "\u00A7");
			if (event.getInventory().getTitle().toLowerCase().contains(ChatColor.stripColor(title.toLowerCase().split(" ")[0])))
			{
				Inventory inv = event.getInventory();
				int i = 0;
				String inventory = "";
				for (ItemStack item : inv)
				{
					if (item != null)
					{
						String enchantments = "";
						if (inventory != "")
							inventory += ", ";
						
						for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
						{
							if (enchantments != "")
								enchantments += ".";
							
							enchantments += entry.getKey().getId() + ";" + entry.getValue();
						}
						inventory += i + ":" + item.getTypeId() + ":" + item.getDurability() + ":" + item.getAmount();
						
						if (item.getItemMeta().getDisplayName() != null)
							inventory += ":" + item.getItemMeta().getDisplayName();
						else
							inventory += ":''";
						
						if (extra.getLore(item) != null)
						{
							String lore = "";
							for (String Lore : item.getItemMeta().getLore())
							{
								if (lore != "")
									lore += "‖";
								
								lore += Lore;
							}
							inventory += ":" + lore;
						}
						else
							inventory += ":''";
						
						if (enchantments != "")
							inventory += ":" + enchantments;
						else
							inventory += ":0";
					}
					i++;
				}
				
				if (plugin.getConfig().getBoolean(plugin.perWorld))
					plugin.getConfig().set("Players." + event.getPlayer().getName() + ".InventoryWorld." + event.getPlayer().getWorld().getName(), inventory);
				else
					plugin.getConfig().set("Players." + event.getPlayer().getName() + ".Inventory", inventory);
				plugin.saveConfig();
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		int size = event.getInventory().getSize() / 9;
		if (size > 0 && size < 7)
		{
			String title = plugin.getConfig().getString("Title." + size).replace("&", "\u00A7");
			
			if (event.getInventory().getTitle().toLowerCase().contains(ChatColor.stripColor(title.toLowerCase().split(" ")[0])))
			{
				Inventory inv = event.getInventory();
				int i = 0;
				String inventory = "";
				for (ItemStack item : inv)
				{
					if (item != null)
					{
						String enchantments = "";
						if (inventory != "")
							inventory += ", ";
						
						for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
						{
							if (enchantments != "")
								enchantments += ".";
							
							enchantments += entry.getKey().getId() + ";" + entry.getValue();
						}
						inventory += i + ":" + item.getTypeId() + ":" + item.getDurability() + ":" + item.getAmount();
						
						if (item.getItemMeta().getDisplayName() != null)
							inventory += ":" + item.getItemMeta().getDisplayName();
						else
							inventory += ":''";
						
						if (extra.getLore(item) != null)
						{
							String lore = "";
							for (String Lore : item.getItemMeta().getLore())
							{
								if (lore != "")
									lore += "‖";
								
								lore += Lore;
							}
							inventory += ":" + lore;
						}
						else
							inventory += ":''";
						
						if (enchantments != "")
							inventory += ":" + enchantments;
						else
							inventory += ":0";
					}
					i++;
				}
				
				inventory = inventory.replace("\\n", "\\n").replace("\n", "\\n");
				
				if (plugin.getConfig().getBoolean(plugin.perWorld))
					plugin.getConfig().set("Players." + event.getWhoClicked().getName() + ".InventoryWorld." + event.getWhoClicked().getWorld().getName(), inventory);
				else
					plugin.getConfig().set("Players." + event.getWhoClicked().getName() + ".Inventory", inventory);
				plugin.saveConfig();
			}
		}
	}
}
