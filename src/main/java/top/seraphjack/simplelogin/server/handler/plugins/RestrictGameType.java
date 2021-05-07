package top.seraphjack.simplelogin.server.handler.plugins;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import top.seraphjack.simplelogin.server.capability.CapabilityLoader;
import top.seraphjack.simplelogin.server.handler.HandlerPlugin;
import top.seraphjack.simplelogin.server.handler.Login;
import top.seraphjack.simplelogin.server.handler.PlayerLoginHandler;
import top.seraphjack.simplelogin.server.storage.Position;
import top.seraphjack.simplelogin.server.storage.SLStorage;

public final class RestrictGameType implements HandlerPlugin {
    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        player.setGameType(GameType.ADVENTURE);
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        if (!PlayerLoginHandler.instance().hasPlayerLoggedIn(login.name)) {
            final Position pos = new Position(player.getPosX(), player.getPosY(), player.getPosZ());
            player.getCapability(CapabilityLoader.CAPABILITY_LAST_POS).ifPresent(c -> c.setLastPos(pos));
        }
        ServerLifecycleHooks.getCurrentServer().deferTask(() -> {
            player.setGameType(SLStorage.instance().storageProvider.gameType(player.getGameProfile().getName()));
        });
    }

    @Override
    public void preLogout(ServerPlayerEntity player) {
        // NO-OP
        // TODO: should we change game type here?
    }
}
