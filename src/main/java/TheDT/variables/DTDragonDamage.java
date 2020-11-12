package TheDT.variables;

import TheDT.cards.AbstractDTCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DTDragonDamage extends DynamicVariable {
	@Override
	public String key() {
		return "DT:THD";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).isDTDragonDamageModified;
		} else {
			return false;
		}
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtDragonDamage;
		} else {
			return 0;
		}
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtBaseDragonDamage;
		} else {
			return 0;
		}
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).upgradedDTDragonDamage;
		} else {
			return false;
		}
	}
}
