# PremiumProxy - Fight piracy by giving legal players something more!

# No longer Works, mojang changed their servers config

Old bukkit text, not exacly describing plugin:

    This is something that you see for the first time, without anycraftbukkit modifications!

**Are you tired of baning players from your offline server?**
**Are you sad that you can't show the game to your best friend because of your online server?**

**Why don't have both!** Allow access some offline players and have different permissions for online and offline players!

Plugin use some simple proxy mechanism to allow every player to game - but also gives a way to detect if player has premium acconut (online) or not!

It's desiggned for servers that want to fight piracy by restricting/disallowing offline or promoting online players.

##### What does it mean to your offline server

    You can give "cash for start" or "more/other permissions" to legal players.

##### What does it mean to your online server

    You can allow to join any player to your online server, for example only as "guest/viewer"

### How to install

- Download PremiumProxy.jar to plugins
- Set online-mode to true
- Restart server

You got server that allows anybody to join, but checks if player is online or offline

- [Option] change proxy port if it is used

### Known problem:

http://mc.dlk.pl/?p=Instrukcje [PL only]

Sumary: Offline Players can't join server with this mod without single hack. This can be done two way:
- using customised MinecraftSP.jar (that can be downloaded from our webside
- adding "fake session server" to *hosts* file

### Why do i need to download Launcher or modify hosts?

**Legal/premium/online players don't need to!**

Others needs a simple workaround (modify hosts destination) because of way how minecraft checks if you're premium. 
By default - server is checking if user is premium, then if yes, client (the game) is doing the same thing on it's own and if it's not premium - it disconnects without any message.

Using this workaround will not allow you to connect to servers that don't allow non-premium users (i.e. don't use PremiumProxy)

### For what is that port in configuration file?

You just set this port to empty/unused port on you server and forget about it!

This port is where our fake session server is running, the craftbukkit ask this port for that if player is online, and this port always responses with "YES" (that's the client-side workaround) PremiumProxy asks real server if this player is Premium after this creates event to announce it to other plugins. This port allows to not modify any craftbukkit code :)

### For developers

It is possible that you will receive event before or after player login event, so keep that in mind.

The only thing you need to do is do catch PremiumStatusEvent and do with the player what you would like to do.
Example plugin code that changes player group (using Vault) from "guest" to "player" if online:

    @EventHandler(priority = EventPriority.NORMAL)
     public void playergroupchange(me.kolorafa.premiumproxy.PremiumStatusEvent event) {
     if(event.hasPremium()){
      //Your code what to do if player is legal
     }
    }


## Known addons:

- PremiumGroupChanger - Example plugin that changes player group from "nonpremium" to "premium" if Joined player is Premium (online) user.
- Premium Money - Plugin gives money to premium players on first join, prevents players from joining multiple times with fake names to gain money ;)
- Make your own and PM me :)

## Something doesn't work? Create ticket!
