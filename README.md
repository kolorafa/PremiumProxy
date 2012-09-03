PremiumProxy
============

PremiumProxy - Fight piracy by giving legal players something more!


Old bukkit text, not exacly describing plugin:

Latest dev version: [PremiumProxy](https://github.com/kolorafa/PremiumProxy/blob/master/dist/PremiumProxy.jar?raw=true)

This is something that you see for the first time, without any
craftbukkit modifications!

Are you tired of baning players from your offline server?

Are you sad that you can't show the game to your best friend because of your online server?

Why don't have both! Allow access some offline players and have different
permissions for online and offline players!

Plugin use some simple proxy mechanism to allow every player to game
and verify player premium status from minecraft.net, we don't promote offline 
servers but only makeing ability to change offline to online without
kicking everyone from the server.

It's for servers that want to fight piracy by restricting/disallowing offline
 or promoting online players.

While chaneing from offline to online, offline players will have some 
difficults to join.

This is only a development base for your ideas !
What does it mean to your offline server

    You can give "cash for start" or "more permissions" to legal players.

What does it mean to your online server

    You can allow to join any player to your online server for example
only as "guest/viewer"

How to install

    Download PremiumProxy.jar to plugins
    Set online-mode to true
    Restart serwer
    You got server that allows anybody to server but checks if players
is online or offline
    Optionally change proxy port if it is used

Know problem:

http://mc.dlk.pl/?p=Instrukcje

Sumary: Offline Players can't join server with this mod without one
(and only one for every server on the net) modification. He need to
add a "fake session server" to hosts, or use Customized
MinecraftSP.jar (you can download it from our server website)
Why do i need to download Launcher or modify hosts

Legal/premium/online players don't need to!

Others needs a simple workaround (modify hosts destination) to go
around client checking if they are premium, YES the clients ask the
server if they are online, it they are the client ask minecraft.net it
they are premium, if not the client(the game) disconnects from server
without saying a word. But yes, You need a workaround (this plugin) on
server too, but they are not connected. And client workaround doesn't
allow you to join online server it they doesn't allow to (doesn't have
this plugin or other craftbukkit modifications)
For what is that port in configuration

You just set this port to empty/unused port on you server and forget about it!

The port is where my plugin redirects session checks, the craftbukkit
ask this port for that if player is online, and this port always
responses with "YES" but plugin catch that request and asking real
server if this player is premium after this creates event to announce
it to other plugins. This port allows to not modify any craftbukkit
code :)
For developers

It is possible that you will receive event before or after player
login event, so keep that in mind.

The only thing you need to do is do catch PremiumStatusEvent and do
with the player what you would like to do, example plugin code that
changes player group (using Vault) from "guest" to "player" if online:

@EventHandler(priority = EventPriority.NORMAL)
public void playergroupchange(PremiumStatusEvent event) {
 if(event.hasPremium()){
  String group =
perms.getPrimaryGroup(getServer().getPlayerExact(event.getPlayerName()));
  if(group.equalsIgnoreCase("guest")){
   perms.playerAddGroup((String)null, event.getPlayerName(), "player");
  }
 }
}

PremiumProxy - Main plugin that check for Premium players

Known addons:

    PremiumGroupChanger - Example plugin that changes player group
from "nonpremium" to "premium" if Joined player is Premium (online)
user.
    Premium Money - Plugin gives money to premium players on first
join, prevents players from joining multiple times with fake names to
gain money ;)
    Make your own and PM me :)

Something doesn't work? Create ticket!