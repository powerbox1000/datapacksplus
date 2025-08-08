package io.github.powerbox1000.datapacksplus.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.dialog.action.CustomAll;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CustomAll.class)
public interface CustomAllAccessor {
    @Accessor("additions")
    @Mutable
    public abstract void setAdditions(Optional<CompoundTag> additions);
}
