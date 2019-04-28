package TheDT.cards;

import basemod.abstracts.CustomCard;

public abstract class AbstractDTCard extends CustomCard {
	public int dtMythicalDamage;
	public int dtBaseMythicalDamage;
	public boolean upgradedDTMythicalDamage;
	public boolean isDTMythicalDamageModified;

	public int dtMythicalBlock;
	public int dtBaseMythicalBlock;
	public boolean upgradedDTMythicalBlock;
	public boolean isDTMythicalBlockModified;

	public AbstractDTCard(final String id,
                           final String name,
                           final String img,
                           final int cost,
                           final String rawDescription,
                           final CardType type,
                           final CardColor color,
                           final CardRarity rarity,
                           final CardTarget target) {

		super(id, name, img, cost, rawDescription, type, color, rarity, target);

		dtMythicalDamage = dtBaseMythicalDamage = -1;
		dtMythicalBlock = dtBaseMythicalBlock = -1;
		isDTMythicalDamageModified = false;
		isDTMythicalBlockModified = false;
	}

	@Override
	public void displayUpgrades() {
		super.displayUpgrades();
		if (upgradedDTMythicalDamage) {
			dtMythicalDamage = dtBaseMythicalDamage;
			isDTMythicalDamageModified = true;
		}
		if (upgradedDTMythicalBlock) {
			dtMythicalBlock = dtBaseMythicalBlock;
			isDTMythicalBlockModified = true;
		}
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		if (dtBaseMythicalDamage != -1) {
			// TODO: Do Mythical Damage powerups
			dtMythicalDamage = dtBaseMythicalDamage;
		}
		if (dtBaseMythicalBlock != -1) {
			// TODO: Do Mythical Block powerups
			dtMythicalBlock = dtBaseMythicalBlock;
		}
	}

	public void upgradeDTMythicalDamage(int amount) {
		dtBaseMythicalDamage += amount;
		dtMythicalDamage = dtBaseMythicalDamage;
		upgradedDTMythicalDamage = true;
	}

	public void upgradeDTMythicalBlock(int amount) {
		dtBaseMythicalBlock += amount;
		dtMythicalBlock = dtBaseMythicalBlock;
		upgradedDTMythicalBlock = true;
	}
}
