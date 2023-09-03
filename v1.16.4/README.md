# ViaMCP
ViaVersion VersionSwitcher for Minecraft Coder Pack (MCP)

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/FlorianMichael/ViaMCP/issues).  
If you just want to talk or need help with ViaMCP feel free to join my
[Discord](https://discord.gg/BwWhCHUKDf).

## 1.16.4 Setup

Firstly, you will need to add the listed libraries into your dependencies in IntelliJ or Eclipse

Dependencies (Included inside ``libraries`` folder)
```
ViaVersion-[ver].jar > ViaVersion > https://github.com/ViaVersion/ViaVersion
ViaBackwards-[ver].jar > ViaBackwards > https://github.com/ViaVersion/ViaBackwards
ViaRewind-[ver].jar > ViaRewind > https://github.com/ViaVersion/ViaRewind
snakeyml-2.0.jar > SnakeYaml > https://bitbucket.org/snakeyaml/snakeyaml
```

Secondly, you need to add code that allows you to actually use ViaMCP (**Choose the version folder that corresponds with your client version**)

### Main-Class
Load the ViaMCP by adding the following code to your main class (or `Minecraft.java`)
```diff
// Minecraft#init(...)

...
+ ViaLoader.load();
if (s != null)
{
    this.displayGuiScreen(new ConnectingScreen(new MainMenuScreen(), this, s, i));
}
...
```

### NetworkManager
You will need to change 2 functions in NetworkManager.java <br>
**1. Hook ViaVersion into the Netty Pipeline**

```diff
// NetworkManager#createNetworkManagerAndConnect

p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", networkmanager);
+ if (p_initChannel_1_ instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion().getVersion() != ViaMCP.NATIVE_VERSION) {
+     final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
+     new ProtocolPipelineImpl(user);
+     
+     p_initChannel_1_.pipeline().addLast(new MCPVLBPipeline(user));
+ }
```

**2. Fix the compression in the NetworkManager#setCompressionThreshold function**
```diff
// NetworkManager#setCompressionThreshold
    ...
+     this.channel.pipeline().fireUserEventTriggered(new CompressionReorderEvent());
} // end of setCompressionThreshold
```

## Version Slider
You will need to add a button to access the protocol switcher (or alternatively use the version slider under this section) <br>
```diff
// MultiplayerScreen#init
    ...
+     this.addButton(ViaMCP.INSTANCE.getAsyncVersionSlider().getButton());
    this.<...>();
} // end of init
```

## Clientside Fixes
### Attack Order Fixes
**Class: Minecraft.java** <br>
**Function: clickMouse()** <br>

```diff
...
-     this.player.swingArm(Hand.MAIN_HAND);
+     AttackOrder.sendConditionalSwing(this.objectMouseOver, Hand.MAIN_HAND);
}
...
```

```diff
    case ENTITY:
-         this.playerController.attackEntity(this.player, ((EntityRayTraceResult) this.objectMouseOver).getEntity());
+         AttackOrder.sendFixedAttack(this.player, ((EntityRayTraceResult) this.objectMouseOver).getEntity(), Hand.MAIN_HAND);
        break;
    ...
```


## Exporting Without JAR Files
This should fix most peoples issues with dependencies (usually NoClassDefFoundError or ClassNotFoundException)

- First export your client normally
- Open your client .jar file with an archive program (winrar or 7zip for example)
- Also open all libraries with the selected archive program (ViaVersion, ViaBackwards, ViaRewind and SnakeYaml)
- From ViaBackwards drag and drop ``assets`` and ``com`` folders to your client .jar
- From ViaRewind drag and drop ``assets`` and ``de`` folders to your client .jar
- From ViaSnakeYaml drag and drop ``org`` folder to your client .jar
- From ViaVersion drag and drop ``assets``, ``com`` and ``us`` folders to your client .jar 
- Then save and close, now your client should be working correctly ;)
