package com.matejdro.bukkit.monsterhunt;

public enum Setting {

    StartTime("StartTime", 13000),
    EndTime("EndTime", 23600),
    DeathPenalty("DeathPenalty", 80),
    TellTime("TellTime", false),
    EnableSignup("EnableSignup", false),
    MinimumPlayers("MinimumPlayers", 2),
    StartChance("StartChance", 100),
    SkipDays("SkipDays", 0),
    SignUpPeriodTime("SignUpPeriodTime", 5),
    AllowSignUpAfterStart("AllowSignUpAfterStart", true),
    EnabledWorlds("EnabledWorlds", MonsterHunt.instance.getServer().getWorlds().get(0).getName()),
    OnlyCountMobsSpawnedOutside("OnlyCountMobsSpawnedOutside", false),
    OnlyCountMobsSpawnedOutsideHeightLimit("OnlyCountMobsSpawnedOutsideHeightLimit", 0),
    OnlyCountMobsSpawnedOutsideBlackList("OnlyCountMobsSpawnedOutsideBlackList", true),
    SkipToIfFailsToStart("SkipToIfFailsToStart", -1),
    AnnounceLead("AnnounceLead", true),
    SelectionTool("SelectionTool", 268),
    HuntZoneMode("HuntZoneMode", false),
    AnnounceSignUp("AnnounceSignUp", false),

    EnableReward("Rewards.EnableReward", false),
    EnableRewardEveryonePermission("Rewards.EnableRewardEveryonePermission", false),
    RewardEveryone("Rewards.RewardEveryone", false),
    NumberOfWinners("Rewards.NumberOfWinners", 3),
    RewardParametersEveryone("Rewards.RewardParametersEveryone", "3 1-1"),
    MinimumPointsEveryone("MinimumPointsEveryone", 1),
    MinimumPointsPlace("MinimumPointsPlace", "", false),
    RewardParametersPlace("Rewards.RewardParametersPlace", "", false),

    UseMySQL("Database.UseMySQL", false),
    MySQLConn("Database.MySQLConn", "jdbc:mysql://localhost:3306/minecraft"),
    MySQLUsername("Database.MySQLUsername", "root"),
    MySQLPassword("Database.MySQLPassword", "password"),

    Debug("Debug", false),

    HuntZoneFirstCorner("HuntZone.FirstCorner", "0,0,0"),
    HuntZoneSecondCorner("HuntZone.SecondCorner", "0,0,0"),
    HuntZoneTeleportLocation("HuntZone.TeleportLocation", "0,0,0"),
    HuntZoneWorld("HuntZone.World", MonsterHunt.instance.getServer().getWorlds().get(0).getName()),

    StartMessage("Messages.StartMessage", "&8[&7!&8]&2Monster Hunt have started in <World>!"),
    FinishMessageWinners("Messages.FinishMessageWinners", "&8[&7!&8]&2Sun is rising, so monster Hunt is finished in <World>! Winners of the today's match are: [NEWLINE] &71st place: &3<NamesPlace1> &9(<PointsPlace1> points) [NEWLINE] &72nd place: &3<NamesPlace2> &9(<PointsPlace2> points) [NEWLINE] &73rd place: &3<NamesPlace3> &9(<PointsPlace3> points)"),
    KillMessageGeneral("Messages.KillMessageGeneral", "&8[&7!&8]&7You killed <MobName> (<MobValue>). You have &3<Points> &7points so far."),
    KillMessageWolf("Messages.KillMessageWolf", "&8[&7!&8]&7Your wolf killed <MobName> (<MobValue>). You have &3<Points> &7points so far."),
    KillMessageArrow("Messages.KillMessageArrow", "&8[&7!&8]&7You killed <MobName> with an arrow (<MobValue>). You have &3<Points> &7points so far."),
    KillMessageSnowball("Messages.KillMessageSnowball", "&8[&7!&8]&7You killed <MobName> with a snowball(<MobValue>). You have &3<Points> &7points so far."),
    RewardMessage("Messages.RewardMessage", "&8[&7!&8]&2Congratulations! You have received &3<Items>"),
    DeathMessage("Messages.DeathMessage", "&8[&7!&8]You have died, so your Monster Hunt score is reduced by &380%."),
    SignUpBeforeHuntMessage("Messages.SignupBeforeHuntMessage", "&8[&7!&8]&7You have signed up for the next hunt in <World>!"),
    SignUpAfterHuntMessage("Messages.SignupAtHuntMessage", "&8[&7!&8]&7You have signed up for the hunt in <World>."),
    HighScoreMessage("Messages.HighScoreMessage", "&8[&7!&8]&6You have reached a new high score: &3<Points> &6points![NEWLINE]&7For more information, try: &3/huntscore [player|top|rank]"),
    FinishMessageNotEnoughPoints("Messages.FinishMessageNotEnoughPoints", "&8[&7!&8]&7Sun is rising, so monster Hunt is finished in <World>! Unfortunately nobody killed enough monsters, so there is no winner."),
    FinishMessageNotEnoughPlayers("Messages.FinishMessageNotEnoughPlayers", "&8[&7!&8]&7Sun is rising, so monster Hunt is finished in <World>! Unfortunately there were not enough players participating, so there is no winner."),
    MessageSignUpPeriod("Messages.MessageSignUpPeriod", "&8[&7!&8]&7Monster Hunt will begin shorty in <World>."),
    MessageTooLateSignUp("Messages.MessageTooLateSignUp", "&8[&7!&8]&7You are too late to sign up."),
    MessageAlreadySignedUp("Messages.MessageAlreadySignedUp", "&8[&7!&8]&7You are already signed up!"),
    MessageStartNotEnoughPlayers("Messages.MessageStartNotEnoughPlayers", "&8[&7!&8]&7Not enougth players to start Monster Hunt. "),
    KillMobSpawnedInsideMessage("Messages.KillMobSpawnedInsideMessage", "&8[&7!&8]&7Your kill was not counted. Stop grinding in caves and go outside!"),
    MessageHuntStatusNotActive("Messages.MessageHuntStatusNotActive", "&8[&7!&8]&7Hunt is currently not active anywhere"),
    MessageHuntStatusHuntActive("Messages.MessageHuntStatusHuntActive", "&8[&7!&8]&2Hunt is active in <Worlds>"),
    MessageHuntStatusLastScore("Messages.MessageHuntStatusLastScore", "&8[&7!&8]&7Your last score in this world was &3<Points> &7points"),
    MessageHuntStatusNotInvolvedLastHunt("Messages.MessageHuntStatusNotInvolvedLastHunt", "&8[&7!&8]&7You were not involved in last hunt in this world"),
    MessageHuntStatusNoKills("Messages.MessageHuntStatusNoKills", "&8[&7!&8]&7You haven't killed any mob in this world's hunt yet."),
    MessageHuntStatusCurrentScore("Messages.MessageHuntStatusCurrentScore", "&8[&7!&8]&7Your current score in this world's hunt is &3<Points> &7points!"),
    MessageHuntStatusTimeReamining("Messages.MessageHuntStatusTimeReamining", "&8[&7!&8]&7You have only &3<Timeleft>% &7of the night left in this world!"),
    MessageLead("Messages.MessageLead", "&8[&7!&8]&9<Player> &2has just taken over lead with &3<Points> &2points!"),
    MessageHuntTeleNoHunt("Messages.MessageHuntTeleNoHunt", "&8[&7!&8]&7You cannot teleport to hunt zone when there is no hunt!"),
    MessageHuntTeleNotSignedUp("Messages.MessageHuntTeleNotSignedUp", "&8[&7!&8]&7You cannot teleport to hunt zone if you are not signed up to the hunt!"),
    SignUpAnnouncement("Messages.SignUpAnnouncement", "&8[&7!&8]&9<Player> &7has signed up for the hunt in <World>!");

    private String name;
    private Object def;
    private boolean WriteDefault;

    private Setting(String Name, Object Def) {
        name = Name;
        def = Def;
        WriteDefault = true;
    }

    private Setting(String Name, Object Def, boolean WriteDefault) {
        name = Name;
        def = Def;
        this.WriteDefault = WriteDefault;
    }

    public String getString() {
        return name;
    }

    public Object getDefault() {
        return def;
    }

    public boolean writeDefault() {
        return WriteDefault;
    }
}
