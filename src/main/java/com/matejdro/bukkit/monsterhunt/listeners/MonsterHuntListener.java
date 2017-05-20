package com.matejdro.bukkit.monsterhunt.listeners;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.matejdro.bukkit.monsterhunt.HuntWorldManager;
import com.matejdro.bukkit.monsterhunt.HuntZone;
import com.matejdro.bukkit.monsterhunt.HuntZoneCreation;
import com.matejdro.bukkit.monsterhunt.MonsterHuntWorld;
import com.matejdro.bukkit.monsterhunt.Setting;
import com.matejdro.bukkit.monsterhunt.Settings;
import com.matejdro.bukkit.monsterhunt.Util;

public class MonsterHuntListener implements Listener {
    //HashMap<Integer, Player> lastHits = new HashMap<Integer, Player>();
    //HashMap<Integer, Integer> lastHitCauses = new HashMap<Integer, Integer>();

    @EventHandler()
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            MonsterHuntWorld world = HuntWorldManager.getWorld(player.getWorld().getName());

            if (world == null || world.getWorld() == null) {
                return;
            }
            if (world.settings.getInt(Setting.DeathPenalty) == 0) {
                return;
            }
            if (world.state > 1 && world.Score.containsKey(player.getName())) {
                double score = world.Score.get(player.getName()) + 0.00;
                score = score - (score * world.settings.getInt(Setting.DeathPenalty) / 100.00);
                world.Score.put(player.getName(), (int) Math.round(score));
                Util.Message(world.settings.getString(Setting.DeathMessage), player);
            }
        }
        if (!HuntZone.isInsideZone(event.getEntity().getLocation())) {
            return;
        }
        if (event.getEntity() == null || !(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
            return;
        }
        MonsterHuntWorld world = HuntWorldManager.getWorld(event.getEntity().getWorld().getName());
        if (world == null || world.getWorld() == null || world.state < 2) {
            return;
        }
        //Anti grinder check
        //check if their head is inside a block
        if (event.getEntity().getEyeLocation().getBlock().getType() != Material.AIR){
            return;
        }
     // no loot for monsters which die standing in water, to make building grinders even more difficult
        Block block = event.getEntity().getLocation().getBlock();

            if (block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)){
                return;
        }

        // also no loot for monsters who can't reach their (melee) killers
        Player killer = event.getEntity().getKiller();
        if (killer != null)
        {
            Location monsterEyeLocation = event.getEntity().getEyeLocation();
            Location playerEyeLocation = killer.getEyeLocation();

            // interpolate locations
            Location[] locations = new Location[]{
                    new Location(monsterEyeLocation.getWorld(), 0.2 * monsterEyeLocation.getX() + 0.8 * playerEyeLocation.getX(),
                            monsterEyeLocation.getY(), 0.2 * monsterEyeLocation.getZ() + 0.8 * playerEyeLocation.getZ()),
                    new Location(monsterEyeLocation.getWorld(), 0.5 * monsterEyeLocation.getX() + 0.5 * playerEyeLocation.getX(),
                            monsterEyeLocation.getY(), 0.5 * monsterEyeLocation.getZ() + 0.5 * playerEyeLocation.getZ()),
                    new Location(monsterEyeLocation.getWorld(), 0.8 * monsterEyeLocation.getX() + 0.2 * playerEyeLocation.getX(),
                            monsterEyeLocation.getY(), 0.8 * monsterEyeLocation.getZ() + 0.2 * playerEyeLocation.getZ()),};

            for (Location middleLocation : locations)
            {
                // monster is blocked at eye level, unable to advance toward killer
                if (middleLocation.getBlock().getType() != Material.AIR)
                    return;
                    // monster doesn't have room above to hurdle a foot level block, unable to advance toward killer
                else
                {
                    Block bottom = middleLocation.getBlock().getRelative(BlockFace.DOWN);
                    Block top = middleLocation.getBlock().getRelative(BlockFace.UP);
                    if (top.getType() != Material.AIR &&
                            bottom.getType() != Material.AIR
                            || bottom.getType() == Material.FENCE
                            || bottom.getType() == Material.FENCE_GATE
                            || bottom.getType() == Material.COBBLE_WALL
                            || bottom.getType() == Material.NETHER_FENCE)
                    {
                        return;
                    }
                }
            }
        }
        
        kill((LivingEntity) event.getEntity(), world);
    }

    private void kill(LivingEntity monster, MonsterHuntWorld world) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) monster.getLastDamageCause();
        String name;
        Player player = null;

        String cause = "General";
        if (event.getCause() == DamageCause.PROJECTILE && event.getDamager() instanceof Projectile) {
            if (event.getDamager() instanceof Snowball) {
                cause = "Snowball";
            } else {
                cause = "Arrow";
            }
            ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
            if (shooter instanceof Player) {
                player = (Player) shooter;
            }
        } else if (event.getDamager() instanceof Wolf && ((Wolf) event.getDamager()).isTamed()) {
            cause = "Wolf";
            player = (Player) ((Wolf) event.getDamager()).getOwner();
        }

        if (player == null) {
            if (!(event.getDamager() instanceof Player))
                return;
            player = (Player) event.getDamager();

            if (cause.equals("General")) {
                if (player.getItemInHand() == null) {
                    cause = String.valueOf(0);
                } else {
                    cause = String.valueOf(player.getItemInHand().getTypeId());
                }
            }
        }

        int points = 0;
        if (monster instanceof Skeleton) {
            points = world.settings.getMonsterValue("Skeleton", cause);
            name = "Skeleton";
        } else if (monster instanceof Spider) {
            points = world.settings.getMonsterValue("Spider", cause);
            name = "Spider";
        } else if (monster instanceof Creeper) {
            Creeper creeper = (Creeper) monster;
            if (creeper.isPowered()) {
                points = world.settings.getMonsterValue("ElectrifiedCreeper", cause);
                name = "Electrified Creeper";
            } else {
                points = world.settings.getMonsterValue("Creeper", cause);
                name = "Creeper";
            }
        } else if (monster instanceof Ghast) {
            points = world.settings.getMonsterValue("Ghast", cause);
            name = "Ghast";
        } else if (monster instanceof Slime) {
            points = world.settings.getMonsterValue("Slime", cause);
            name = "Slime";
        } else if (monster instanceof PigZombie) {
            points = world.settings.getMonsterValue("ZombiePigman", cause);
            name = "Zombie Pigman";
        } else if (monster instanceof Giant) {
            points = world.settings.getMonsterValue("Giant", cause);
            name = "Giant";
        } else if (monster instanceof Zombie) {
            points = world.settings.getMonsterValue("Zombie", cause);
            name = "Zombie";
        } else if (monster instanceof Wolf) {
            Wolf wolf = (Wolf) monster;
            if (wolf.isTamed()) {
                points = world.settings.getMonsterValue("TamedWolf", cause);
                name = "Tamed Wolf";
            } else {
                points = world.settings.getMonsterValue("WildWolf", cause);
                name = "Wild Wolf";
            }

        } else if (monster instanceof Player) {
            points = world.settings.getMonsterValue("Player", cause);
            name = "Player";
        } else if (monster instanceof Enderman) {
            points = world.settings.getMonsterValue("Enderman", cause);
            name = "Enderman";
        } else if (monster instanceof Silverfish) {
            points = world.settings.getMonsterValue("Silverfish", cause);
            name = "Silverfish";
        } else if (monster instanceof CaveSpider) {
            points = world.settings.getMonsterValue("CaveSpider", cause);
            name = "CaveSpider";
        } else if (monster instanceof EnderDragon) {
            points = world.settings.getMonsterValue("EnderDragon", cause);
            name = "Ender Dragon";
        } else if (monster instanceof MagmaCube) {
            points = world.settings.getMonsterValue("MagmaCube", cause);
            name = "Magma Cube";
        } else if (monster instanceof MushroomCow) {
            points = world.settings.getMonsterValue("Mooshroom", cause);
            name = "Mooshroom";
        } else if (monster instanceof Chicken) {
            points = world.settings.getMonsterValue("Chicken", cause);
            name = "Chicken";
        } else if (monster instanceof Cow) {
            points = world.settings.getMonsterValue("Cow", cause);
            name = "Cow";
        } else if (monster instanceof Blaze) {
            points = world.settings.getMonsterValue("Blaze", cause);
            name = "Blaze";
        } else if (monster instanceof Pig) {
            points = world.settings.getMonsterValue("Pig", cause);
            name = "Pig";
        } else if (monster instanceof Sheep) {
            points = world.settings.getMonsterValue("Sheep", cause);
            name = "Sheep";
        } else if (monster instanceof Snowman) {
            points = world.settings.getMonsterValue("SnowGolem", cause);
            name = "Snow Golem";
        } else if (monster instanceof Squid) {
            points = world.settings.getMonsterValue("Squid", cause);
            name = "Squid";
        } else if (monster instanceof Villager) {
            points = world.settings.getMonsterValue("Villager", cause);
            name = "Villager";
        } else if (monster instanceof Guardian) {
            points = world.settings.getMonsterValue("Guardian", cause);
            name = "Guardian";
        } else if (monster instanceof Shulker) {
            points = world.settings.getMonsterValue("Shulker", cause);
            name = "Shulker";
        } else if (monster instanceof PolarBear) {
            points = world.settings.getMonsterValue("PolarBear", cause);
            name = "PolarBear";
        } else if (monster instanceof Endermite) {
            points = world.settings.getMonsterValue("Endermite", cause);
            name = "Endermite";
        } else if (monster instanceof Witch) {
            points = world.settings.getMonsterValue("Witch", cause);
            name = "Witch";
        } else if (monster instanceof Vex) {
            points = world.settings.getMonsterValue("Vex", cause);
            name = "Vex";
        } else if (monster instanceof Vindicator) {
            points = world.settings.getMonsterValue("Vindicator", cause);
            name = "Vindicator";
        } else if (monster instanceof Evoker) {
            points = world.settings.getMonsterValue("Evoker", cause);
            name = "Evoker";
        } else if (monster instanceof Illusioner) {
            points = world.settings.getMonsterValue("Illusioner", cause);
            name = "Illusioner";
        } else if (monster instanceof Wither) {
            points = world.settings.getMonsterValue("Wither", cause);
            name = "Wither";
        } else {
            return;
        }
        if (points < 1) {
            return;
        }

        if (!world.Score.containsKey(player.getName()) && !world.settings.getBoolean(Setting.EnableSignup)) {
            world.Score.put(player.getName(), 0);
        }
        if (world.Score.containsKey(player.getName())) {
            if (!(world.settings.getBoolean(Setting.OnlyCountMobsSpawnedOutsideBlackList) ^ world.properlyspawned.contains(monster.getEntityId())) && world.settings.getBoolean(Setting.OnlyCountMobsSpawnedOutside)) {
                String message = world.settings.getString(Setting.KillMobSpawnedInsideMessage);
                Util.Message(message, player);
                world.blacklist.add(monster.getEntityId());
                return;
            }
            int newscore = world.Score.get(player.getName()) + points;

            if (world.settings.getBoolean(Setting.AnnounceLead)) {
                Entry<String, Integer> leadpoints = null;
                for (Entry<String, Integer> e : world.Score.entrySet()) {
                    if (leadpoints == null || e.getValue() > leadpoints.getValue() || (e.getValue() == leadpoints.getValue() && leadpoints.getKey().equalsIgnoreCase(player.getName()))) {
                        leadpoints = e;
                    }
                }
                Util.Debug(leadpoints.toString());
                Util.Debug(String.valueOf(newscore));
                Util.Debug(String.valueOf(!leadpoints.getKey().equals(player.getName())));

                if (leadpoints != null && newscore > leadpoints.getValue() && !leadpoints.getKey().equals(player.getName())) {
                    String message = world.settings.getString(Setting.MessageLead);
                    message = message.replace("<Player>", player.getName());
                    message = message.replace("<Points>", String.valueOf(newscore));
                    message = message.replace("<World>", world.name);
                    Util.Broadcast(message);
                }
            }

            world.Score.put(player.getName(), newscore);
            world.blacklist.add(monster.getEntityId());

            world.properlyspawned.remove((Object) monster.getEntityId());

            String message = world.settings.getKillMessage(cause);
            message = message.replace("<MobValue>", String.valueOf(points));
            message = message.replace("<MobName>", name);
            message = message.replace("<Points>", String.valueOf(newscore));
            Util.Message(message, player);
        }
    }

    @EventHandler()
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Creature) {
            MonsterHuntWorld world = HuntWorldManager.getWorld(event.getLocation().getWorld().getName());
            if (world == null || world.getWorld() == null) {
                return;
            }
            if (world.state == 0) {
                return;
            }
            if (!world.settings.getBoolean(Setting.OnlyCountMobsSpawnedOutside)) {
                return;
            }
            Block block = event.getLocation().getBlock();
            int number = 0;
            while (block.getY() < 125) {
                number++;
                block = block.getRelative(BlockFace.UP);
                boolean empty = false;

                if (block.getType() == Material.AIR || block.getType() == Material.LEAVES) {
                    empty = true;
                } else if (block.getType() == Material.LOG) {
                    if (block.getRelative(BlockFace.NORTH).getType() == Material.AIR || block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES) {
                        empty = true;
                    } else if (block.getRelative(BlockFace.EAST).getType() == Material.AIR || block.getRelative(BlockFace.EAST).getType() == Material.LEAVES) {
                        empty = true;
                    } else if (block.getRelative(BlockFace.WEST).getType() == Material.AIR || block.getRelative(BlockFace.WEST).getType() == Material.LEAVES) {
                        empty = true;
                    } else if (block.getRelative(BlockFace.SOUTH).getType() == Material.AIR || block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) {
                        empty = true;
                    } else if (block.getRelative(BlockFace.UP).getType() == Material.AIR || block.getRelative(BlockFace.UP).getType() == Material.LEAVES) {
                        empty = true;
                    } else if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR || block.getRelative(BlockFace.DOWN).getType() == Material.LEAVES) {
                        empty = true;
                    }
                }

                if (!empty) {
                    if (world.settings.getBoolean(Setting.OnlyCountMobsSpawnedOutsideBlackList)) {
                        world.properlyspawned.add(event.getEntity().getEntityId());
                    }
                    return;
                }
                if (world.settings.getInt(Setting.OnlyCountMobsSpawnedOutsideHeightLimit) > 0 && world.settings.getInt(Setting.OnlyCountMobsSpawnedOutside) < number) {
                    break;
                }
            }
            if (!world.settings.getBoolean(Setting.OnlyCountMobsSpawnedOutsideBlackList)) {
                world.properlyspawned.add(event.getEntity().getEntityId());
            }
        }
    }

    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getTypeId() == Settings.globals.getInt(Setting.SelectionTool.getString(), 268)) {
            if (HuntZoneCreation.players.containsKey(event.getPlayer().getName())) {
                HuntZoneCreation.select(event.getPlayer(), event.getClickedBlock());
                event.setCancelled(true);
            }
        }
    }
}