package TheDT.variables;

import TheDT.cards.AbstractDTCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DTDragonBlock extends DynamicVariable {
	@Override
	public String key() {
		return "DT:THB";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).isDTDragonBlockModified;
		} else {
			return false;
		}
	}

	@Override
	public void setIsModified(AbstractCard card, boolean v) {
		if (card instanceof AbstractDTCard) {
			((AbstractDTCard) card).isDTDragonBlockModified = v;
		}
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtDragonBlock;
		} else {
			return 0;
		}
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtBaseDragonBlock;
		} else {
			return 0;
		}
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).upgradedDTDragonBlock;
		} else {
			return false;
		}
	}
}
