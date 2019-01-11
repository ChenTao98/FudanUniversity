package sdfs.filetree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockInfo implements Iterable<LocatedBlock>,Serializable {

    private final List<LocatedBlock> locatedBlocks = new ArrayList<>();

    @Override
    public Iterator<LocatedBlock> iterator() {
        return locatedBlocks.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockInfo that = (BlockInfo) o;

        return locatedBlocks.equals(that.locatedBlocks);
    }

    @Override
    public int hashCode() {
        return locatedBlocks.hashCode();
    }
    public void addLocatedBlock(LocatedBlock locatedBlock){
        locatedBlocks.add(locatedBlock);
    }
}
