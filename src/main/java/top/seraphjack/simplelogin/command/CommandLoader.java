package top.seraphjack.simplelogin.command;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.seraphjack.simplelogin.SLConstants;
import top.seraphjack.simplelogin.command.arguments.ArgumentTypeEntryName;
import top.seraphjack.simplelogin.command.arguments.ArgumentTypeHandlerPlugin;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SLConstants.MODID)
public final class CommandLoader {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, SLConstants.MODID);

    public static final RegistryObject<ArgumentTypeEntryName.Info> ENTRY_NAME_ARGUMENT_TYPE = COMMAND_ARGUMENT_TYPES.register("entry", () ->
            ArgumentTypeInfos.registerByClass(ArgumentTypeEntryName.class, new ArgumentTypeEntryName.Info()));
    public static final RegistryObject<ArgumentTypeHandlerPlugin.Info> PLUGIN_ARGUMENT_TYPE = COMMAND_ARGUMENT_TYPES.register("plugin", () ->
            ArgumentTypeInfos.registerByClass(ArgumentTypeHandlerPlugin.class, new ArgumentTypeHandlerPlugin.Info()));

    @SubscribeEvent
    public static void commandRegister(RegisterCommandsEvent event) {
        SLCommand.register(event.getDispatcher());
    }

}
