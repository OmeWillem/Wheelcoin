package dev.willem.wheelcoin.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public final class ChatUtils {
    public Component parse(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
}
