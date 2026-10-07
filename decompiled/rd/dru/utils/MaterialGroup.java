/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package rd.dru.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;

public class MaterialGroup {
    private Set<String> group = new HashSet<String>();

    public MaterialGroup(Material material) {
        this.group.add(material.name());
    }

    public MaterialGroup(Collection<Material> materials) {
        this.group.addAll(materials.stream().map(m -> m.name()).collect(Collectors.toSet()));
    }

    public MaterialGroup(Material ... material) {
        this(Arrays.asList(material));
    }

    public MaterialGroup(String ... material) {
        this.group.addAll(Arrays.asList(material));
    }

    public void add(Material material) {
        this.group.add(material.name());
    }

    public void remove(Material material) {
        this.group.remove(material.name());
    }

    public Set<Material> getMaterials() {
        return this.group.stream().map(s -> Material.matchMaterial((String)s)).collect(Collectors.toSet());
    }

    public boolean contains(Material material) {
        return this.group.contains(material.name());
    }

    public boolean containsAll(Collection<Material> material) {
        return this.group.containsAll(material.stream().map(s -> s.name()).collect(Collectors.toSet()));
    }
}

