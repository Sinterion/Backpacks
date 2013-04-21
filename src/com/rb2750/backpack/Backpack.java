package com.rb2750.backpack;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Backpack extends JavaPlugin
{
	String perWorld = "Backpacks.per-world";
	String deathDrop = "Backpacks.drop-on-death";
	
	public void onEnable()
	{
		if (new File(getDataFolder().getParentFile().getAbsoluteFile() + "\\Backpacks").exists())
		{
			if (new File(getDataFolder().getParentFile().getAbsoluteFile() + "\\MCPacks").exists())
				deleteDirectory(new File(getDataFolder().getParentFile().getAbsoluteFile() + "\\MCPacks"));
			new File(getDataFolder().getParentFile().getAbsoluteFile() + "\\Backpacks").renameTo(new File(getDataFolder().getParentFile().getAbsoluteFile() + "\\MCPacks"));
		}
		
		try
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
		}
		catch (IOException e)
		{
			System.out.println("Error Submitting stats!");
		}
		
		if (!getConfig().contains("Backpacks.per-world"))
		{
			getConfig().set("Backpacks.per-world", false);
		}
		
		if (!getConfig().contains("Backpacks.drop-on-death"))
		{
			getConfig().set("Backpacks.drop-on-death", true);
		}
		
		if (getConfig().contains("Backpacks.title"))
		{
			getConfig().set("Backpacks.title", null);
		}
		
		for (int i = 1; i <= 6; i++)
		{
			if (!getConfig().contains("Title." + i))
				getConfig().set("Title." + i, "&9Your Backpack");
		}
		
		saveConfig();
		getServer().getPluginManager().registerEvents(new EventHandle(this), this);
	}
	
	public static boolean deleteDirectory(File directory)
	{
		if (directory.exists())
		{
			File[] files = directory.listFiles();
			if (null != files)
			{
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].isDirectory())
					{
						deleteDirectory(files[i]);
					}
					else
					{
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("Backpack"))
		{
			final Player player = (Player) sender;
			
			int slots = 0;
			String title = getConfig().getString("Backpacks.title");
			
			if (player.hasPermission("backpack.1"))
			{
				slots = 1;
			}
			
			if (player.hasPermission("backpack.2"))
			{
				slots = 2;
			}
			
			if (player.hasPermission("backpack.3"))
			{
				slots = 3;
			}
			
			if (player.hasPermission("backpack.4"))
			{
				slots = 4;
			}
			
			if (player.hasPermission("backpack.5"))
			{
				slots = 5;
			}
			
			if (player.hasPermission("backpack.6"))
			{
				slots = 6;
			}
			
			if (getConfig().contains("Title." + slots))
				title = getConfig().getString("Title." + slots);
			else
				getConfig().set("Title." + slots, getConfig().getString("Backpacks.title"));
			
			if (slots > 0)
			{
				if (player.getGameMode() != GameMode.CREATIVE)
				{
					title = title.replace("&", "\u00A7");
					final Inventory inv = Bukkit.createInventory(player, slots * 9, title);
					String split = null;
					
					if (args.length == 1 && player.isOp())
					{
						if (getConfig().contains("Players." + args[0] + ".Inventory"))
						{
							if (getConfig().getBoolean(perWorld))
								split = getConfig().getString("Players." + args[0] + ".InventoryWorld." + player.getWorld().getName());
							else
								split = getConfig().getString("Players." + args[0] + ".Inventory");
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "Could not find a backpack for this player!\nWarning: It is case sensitive!");
							return true;
						}
					}
					else
					{
						if (getConfig().getBoolean(perWorld))
							split = getConfig().getString("Players." + player.getName() + ".InventoryWorld." + player.getWorld().getName());
						else
							split = getConfig().getString("Players." + player.getName() + ".Inventory");
					}
					
					if (split != null)
					{
						if (split.length() != 0)
						{
							String[] parts = split.split(",");
							for (String part : parts)
							{
								String[] data = null;
								int slot = 0;
								int id = 0;
								short damage = 0;
								int amount = 0;
								ItemStack item = null;
								
								try
								{
									part = part.trim();
									data = part.split(":");
									slot = Integer.parseInt(data[0]);
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
									
									inv.setItem(slot, item);
								}
								catch (Exception e)
								{
									try
									{
										id = Integer.parseInt(part.split(";")[0].split(":")[0]);
										damage = Short.parseShort(part.split(";")[0].split(":")[1]);
										amount = Integer.parseInt(part.split(";")[1]);
										item = new ItemStack(id, amount, damage);
										inv.addItem(item);
									}
									catch (Exception ex)
									{
										try
										{
											part = part.trim();
											data = part.split(":");
											slot = Integer.parseInt(data[0]);
											id = Integer.parseInt(data[1]);
											damage = Short.parseShort(data[2]);
											amount = Integer.parseInt(data[3]);
											item = new ItemStack(id, amount, damage);
											if (data.length > 4)
												if (data[4].trim() != "''")
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
											
											inv.setItem(slot, item);
										}
										catch (Exception exc)
										{
											try
											{
												part = part.trim();
												data = part.split(":");
												slot = Integer.parseInt(data[0]);
												id = Integer.parseInt(data[1]);
												damage = Short.parseShort(data[2]);
												amount = Integer.parseInt(data[3]);
												item = new ItemStack(id, amount, damage);
												if (data.length > 4)
													if (!data[4].equals("''"))
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
													if (!data[5].equals("''"))
													{
														String[] lores = data[5].split("‖");
														if (lores.length > 0)
															for (String lore : lores)
																if (lore != "''")
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
												
												inv.setItem(slot, item);
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
					player.openInventory(inv);
					
					if (args.length == 1 && player.isOp())
						sender.sendMessage(ChatColor.GREEN + "Opened " + args[0] + "'s backpack!");
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You cannot use backpacks when in creative!");
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You don't have permission!!");
			}
			return true;
		}
		return false;
	}
}