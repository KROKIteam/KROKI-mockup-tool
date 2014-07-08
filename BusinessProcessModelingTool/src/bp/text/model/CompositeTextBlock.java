package bp.text.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompositeTextBlock extends TextBlock {

    private final List<TextBlock> blocks;

    public CompositeTextBlock() {
        this.blocks = new ArrayList<>();
    }

    @Override
    public Boolean isLeaf() {
        return false;
    }

    public List<TextBlock> getBlocks() {
        return this.blocks;
    }

    @Override
    public String getText() {
        final StringBuilder output = new StringBuilder();
        for (final TextBlock block : this.blocks) {
            output.append(block.getText());
        }
        return output.toString();
    }

    @Override
    public Integer getLength() {
        Integer totalLength = 0;
        for (final TextBlock block : this.blocks) {
            totalLength += block.getLength();
        }
        return totalLength;
    }

    @Override
    public void insertText(final Integer position, final String text) {
        for (final TextBlock block : this.blocks) {
            if (block.getPosition() < position && position > block.getPosition() + block.getLength()) {
                block.insertText(position, text);
            }
        }
    }

    @Override
    public void removeText(final Integer startPos, Integer endPos) {
        final Set<TextBlock> toRemove = new HashSet<>();
        for (int i = this.blocks.size() - 1; i >= 0; i++) {
            final TextBlock block = this.blocks.get(i);
            if (endPos > block.getPosition()) {
                if (startPos >= block.getPosition()) {
                    block.removeText(startPos, endPos);
                    if (block.getLength() == 0) {
                        block.removeBlock();
                        toRemove.add(block);
                    }
                    break;
                } else {
                    block.removeText(block.getPosition(), endPos);
                    endPos = block.getPosition();
                    if (block.getLength() == 0) {
                        block.removeBlock();
                        toRemove.add(block);
                    }
                }
            }
        }
        for (final TextBlock block : toRemove) {
            this.blocks.remove(block);
        }
    }

    public void addTextBlock(final TextBlock block, final Integer pos) {
        if (pos < 0 || pos > this.blocks.size()) {
            return;
        }
        block.setOwner(this);
        if (this.blocks.isEmpty()) {
            block.setPrecessor(null);
            block.setSuccessor(null);
        } else {
            if (pos == this.blocks.size()) {
                final TextBlock b = this.blocks.get(this.blocks.size() - 1);
                block.setPrecessor(b);
                block.setSuccessor(null);
                b.setSuccessor(block);
                this.blocks.add(block);
            } else {
                final TextBlock b = this.blocks.get(pos);
                block.setPrecessor(b.getPrecessor());
                block.setSuccessor(b);
                if (b.getPrecessor() != null) {
                    b.getPrecessor().setSuccessor(block);
                }
                b.setPrecessor(block);
                this.blocks.add(pos, block);
            }
        }

    }
}
