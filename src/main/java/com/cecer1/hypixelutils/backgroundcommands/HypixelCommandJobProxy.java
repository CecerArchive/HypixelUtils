package com.cecer1.hypixelutils.backgroundcommands;

import com.cecer1.hypixelutils.HypixelUtilsCore;
import com.cecer1.hypixelutils.backgroundcommands.callbacks.IHypixelCommandCallbackProxy;
import com.cecer1.hypixelutils.events.eventdata.IEventData;
import com.cecer1.hypixelutils.events.eventdata.OnChatEventData;
import com.cecer1.hypixelutils.events.eventdata.OnChatMessageEventData;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

public class HypixelCommandJobProxy extends HypixelCommandJob {
    private boolean _started = false;
    private boolean _done = false;
    private String _result = null;
    private IHypixelCommandCallbackProxy _callback;
    private static final Pattern _pattern = Pattern.compile("^\\S+-hp-\\S+$");

    public HypixelCommandJobProxy(IHypixelCommandCallbackProxy callback) {
        _callback = callback;
    }

    @Override
    public void onEvent(OnChatMessageEventData message) {
        return;
    }
    
    @Override
    public void onEvent(IEventData data) {
        if(data instanceof OnChatEventData)
            onEvent((OnChatEventData) data);
    }
    
    @Override
    public void onEvent(OnChatEventData event) {
        IChatComponent message = event.getMessage();
        if(message.getSiblings().size() != 0)
            return;
        if(!_pattern.matcher(message.getUnformattedText()).matches())
            return;

        if (message.getChatStyle().getColor() == EnumChatFormatting.WHITE) {
            _result = message.getUnformattedText();
            _done = true;
            event.setCanceled(true);
        }
    }

    @Override
    public void trigger() {
        HypixelUtilsCore.sendChatMessage("/proxy");
        _started = true;
    }


    @Override
    public boolean isStarted() {
        return _started;
    }
    @Override
    public boolean isDone() {
        return _done;
    }

    @Override
    public void complete() {
        _callback.result(_result);
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }
}
