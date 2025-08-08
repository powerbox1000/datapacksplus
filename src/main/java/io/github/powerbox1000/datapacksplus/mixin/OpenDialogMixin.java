package io.github.powerbox1000.datapacksplus.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.core.Holder;
import net.minecraft.nbt.*;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.*;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerPlayer.class)
public abstract class OpenDialogMixin {
    @ModifyVariable(method = "openDialog(Lnet/minecraft/core/Holder;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Holder<Dialog> openDialog(Holder<Dialog> holder) {
        Dialog d = holder.value();

        // Look up dialog type and cast appropriately (once again "thanks" Mojang)
        if (d instanceof SimpleDialog) {
            SimpleDialog dialog = (SimpleDialog) d;
            for (ActionButton actionBtn : dialog.mainActions()) {
                Optional<Action> optional = actionBtn.action();
                if (optional.isPresent()) {
                    Action action = optional.get();

                    // Check if type  is dynamic/custom_action
                    if (action instanceof CustomAll) {
                        CustomAll customAction = (CustomAll) action;
                        Optional<CompoundTag> optional2 = customAction.additions();
                        CompoundTag additions;
                        if (optional2.isPresent()) {
                            additions = optional2.get();
                        } else {
                            additions = new CompoundTag();
                            optional2 = Optional.of(additions);
                            ((CustomAllAccessor) (Object) customAction).setAdditions(optional2);
                        }
                        additions.put("__datapacksplus:playername__", StringTag.valueOf(((ServerPlayer) (Object) this).getName().getString()));
                    }
                }
            }
            return Holder.direct(dialog);
        }

        throw new UnsupportedOperationException("Unsupported dialog type: " + d.getClass().getName());
    }
}
