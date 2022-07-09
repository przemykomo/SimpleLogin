package top.seraphjack.simplelogin.command.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.forgespi.Environment;
import top.seraphjack.simplelogin.server.storage.SLStorage;

import java.util.concurrent.CompletableFuture;

public final class ArgumentTypeEntryName implements ArgumentType<EntryNameInput> {

    private static final DynamicCommandExceptionType ENTRY_NOT_EXIST = new DynamicCommandExceptionType((o -> Component.translatable("simplelogin.command.error.entry_not_found", o)));

    private ArgumentTypeEntryName() {
    }

    public static ArgumentTypeEntryName entryName() {
        return new ArgumentTypeEntryName();
    }

    @Override
    public EntryNameInput parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();

        if (Environment.get().getDist() == Dist.DEDICATED_SERVER && !SLStorage.instance().storageProvider.registered(name)) {
            throw ENTRY_NOT_EXIST.create(name);
        }
        return EntryNameInput.of(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSourceStack) {
            return SharedSuggestionProvider.suggest(SLStorage.instance().storageProvider.getAllRegisteredUsername(), builder);
        } else if (context.getSource() instanceof SharedSuggestionProvider) {
            CommandContext<SharedSuggestionProvider> ctx = (CommandContext<SharedSuggestionProvider>) context;
            return ((SharedSuggestionProvider) context.getSource()).customSuggestion(ctx);
        }
        return Suggestions.empty();
    }

    public static <S> String getEntryName(CommandContext<S> ctx, String name) {
        return ctx.getArgument(name, EntryNameInput.class).getName();
    }

    public static class Info implements ArgumentTypeInfo<ArgumentTypeEntryName, Info.Template> {
        @Override
        public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {

        }

        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            return new Template();
        }

        @Override
        public void serializeToJson(Template template, JsonObject json) {

        }

        @Override
        public Template unpack(ArgumentTypeEntryName argument) {
            return new Template();
        }

        public class Template implements ArgumentTypeInfo.Template<ArgumentTypeEntryName> {

            @Override
            public ArgumentTypeEntryName instantiate(CommandBuildContext commandBuildContext) {
                return entryName();
            }

            @Override
            public ArgumentTypeInfo<ArgumentTypeEntryName, ?> type() {
                return Info.this;
            }
        }
    }
}
