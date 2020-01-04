package TheDT.cards;

import TheDT.DTMod;
import TheDT.characters.Dragon;
import TheDT.characters.TheDT;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.WristBlade;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

public abstract class AbstractDTCard extends CustomCard {
	public enum DTCardTarget {
		DEFAULT, DRAGON_ONLY, BOTH
	}

	private static final String RAW_ID = "AbstractDTCard";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

	public static ArrayList<String> playerPowerApplyToDragon;

	static {
		playerPowerApplyToDragon = new ArrayList<>();
		playerPowerApplyToDragon.add(VigorPower.POWER_ID);
	}

	public int dtDragonDamage;
	public int dtBaseDragonDamage;
	public boolean upgradedDTDragonDamage;
	public boolean isDTDragonDamageModified;

	public int dtDragonBlock;
	public int dtBaseDragonBlock;
	public boolean upgradedDTDragonBlock;
	public boolean isDTDragonBlockModified;

	public DTCardTarget dtCardTarget;

	public AbstractDTCard(String id,
	                      String name,
	                      String img,
	                      int cost,
	                      String rawDescription,
	                      CardType type,
	                      CardColor color,
	                      CardRarity rarity,
	                      CardTarget target,
	                      DTCardTarget dtCardTarget) {

		super(id, name, img, cost, rawDescription, type, color, rarity, target);

		dtDragonDamage = dtBaseDragonDamage = -1;
		dtDragonBlock = dtBaseDragonBlock = -1;
		isDTDragonDamageModified = false;
		isDTDragonBlockModified = false;
		this.dtCardTarget = dtCardTarget;
	}

	@Override
	public void displayUpgrades() {
		super.displayUpgrades();
		if (upgradedDTDragonDamage) {
			dtDragonDamage = dtBaseDragonDamage;
			isDTDragonDamageModified = true;
		}
		if (upgradedDTDragonBlock) {
			dtDragonBlock = dtBaseDragonBlock;
			isDTDragonBlockModified = true;
		}
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		Dragon dragon = getDragon();
		if (dtBaseDragonDamage != -1) {
			dtDragonDamage = dtBaseDragonDamage;
			if (dragon != null) {
				isDTDragonDamageModified = false;
				if (!isMultiDamage) {
					float tmp = (float) dtBaseDragonDamage;

					if (AbstractDungeon.player.hasRelic(WristBlade.ID) && (costForTurn == 0 || freeToPlayOnce)) {
						tmp += 3.0F;
						if (dtBaseDragonDamage != (int) tmp) {
							isDTDragonDamageModified = true;
						}
					}

					for (AbstractPower p : dragon.powers) {
						tmp = p.atDamageGive(tmp, damageTypeForTurn);
						if (dtBaseDragonDamage != (int) tmp) {
							isDTDragonDamageModified = true;
						}
					}
					for (AbstractPower p : AbstractDungeon.player.powers) {
						if (playerPowerApplyToDragon.contains(p.ID)) {
							tmp = p.atDamageGive(tmp, damageTypeForTurn);
							if (dtBaseDragonDamage != (int) tmp) {
								isDTDragonDamageModified = true;
							}
						}
					}
					for (AbstractPower p : dragon.powers) {
						tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
						if (dtBaseDragonDamage != (int) tmp) {
							isDTDragonDamageModified = true;
						}
					}
					for (AbstractPower p : AbstractDungeon.player.powers) {
						if (playerPowerApplyToDragon.contains(p.ID)) {
							tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
							if (dtBaseDragonDamage != (int) tmp) {
								isDTDragonDamageModified = true;
							}
						}
					}

					if (tmp < 0.0F) {
						tmp = 0.0F;
					}

					dtDragonDamage = MathUtils.floor(tmp);
				} else {
					ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
					float[] tmp = new float[m.size()];

					int i;
					for (i = 0; i < tmp.length; ++i) {
						tmp[i] = (float) dtBaseDragonDamage;
					}

					for (i = 0; i < tmp.length; ++i) {
						if (AbstractDungeon.player.hasRelic(WristBlade.ID) && (costForTurn == 0 || freeToPlayOnce)) {
							tmp[i] += 3.0F;
							if (dtBaseDragonDamage != (int) tmp[i]) {
								isDTDragonDamageModified = true;
							}
						}

						for (AbstractPower p : dragon.powers) {
							tmp[i] = p.atDamageGive(tmp[i], damageTypeForTurn);
							if (dtBaseDragonDamage != (int) tmp[i]) {
								isDTDragonDamageModified = true;
							}
						}
					}

					for (i = 0; i < tmp.length; ++i) {
						for (AbstractPower p : dragon.powers) {
							tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
							if (dtBaseDragonDamage != (int) tmp[i]) {
								isDTDragonDamageModified = true;
							}
						}
					}

					for (i = 0; i < tmp.length; ++i) {
						if (tmp[i] < 0.0F) {
							tmp[i] = 0.0F;
						}
					}

					multiDamage = new int[tmp.length];

					for (i = 0; i < tmp.length; ++i) {
						multiDamage[i] = MathUtils.floor(tmp[i]);
					}

					dtDragonDamage = multiDamage[0];
				}
			}
		}
		if (dtBaseDragonBlock != -1) {
			dtDragonBlock = dtBaseDragonBlock;
			if (dragon != null) {
				isDTDragonBlockModified = false;
				float tmp = (float) dtBaseDragonBlock;

				for (AbstractPower p : dragon.powers) {
					tmp = p.modifyBlock(tmp);
					if (dtBaseDragonBlock != MathUtils.floor(tmp)) {
						isDTDragonBlockModified = true;
					}
				}

				if (tmp < 0.0F) {
					tmp = 0.0F;
				}

				dtDragonBlock = MathUtils.floor(tmp);
			}
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);

		Dragon dragon = getDragon();
		if (dtBaseDragonDamage != -1) {
			dtDragonDamage = dtBaseDragonDamage;
			if (dragon != null) {
				isDTDragonDamageModified = false;
				if (!isMultiDamage) {
					float tmp = (float) dtBaseDragonDamage;

					if (AbstractDungeon.player.hasRelic(WristBlade.ID) && (costForTurn == 0 || freeToPlayOnce)) {
						tmp += 3.0F;
						if (dtBaseDragonDamage != (int) tmp) {
							isDTDragonDamageModified = true;
						}
					}

					for (AbstractPower p : dragon.powers) {
						tmp = p.atDamageGive(tmp, damageTypeForTurn);
						if (dtBaseDragonDamage != (int) tmp) {
							isDTDragonDamageModified = true;
						}
					}
					for (AbstractPower p : AbstractDungeon.player.powers) {
						if (playerPowerApplyToDragon.contains(p.ID)) {
							tmp = p.atDamageGive(tmp, damageTypeForTurn);
							if (dtBaseDragonDamage != (int) tmp) {
								isDTDragonDamageModified = true;
							}
						}
					}
					for (AbstractPower p : mo.powers) {
						tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
					}

					for (AbstractPower p : dragon.powers) {
						tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
					}
					for (AbstractPower p : AbstractDungeon.player.powers) {
						if (playerPowerApplyToDragon.contains(p.ID)) {
							tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
						}
					}
					for (AbstractPower p : mo.powers) {
						tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
					}
					if (tmp < 0.0F) {
						tmp = 0.0F;
					}

					dtDragonDamage = MathUtils.floor(tmp);
					if (dtBaseDragonDamage != dtDragonDamage) {
						isDTDragonDamageModified = true;
					}
				} else {
					ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
					float[] tmp = new float[m.size()];

					int i;
					for (i = 0; i < tmp.length; ++i) {
						tmp[i] = (float) dtBaseDragonDamage;
					}

					for (i = 0; i < tmp.length; ++i) {
						if (AbstractDungeon.player.hasRelic(WristBlade.ID) && (costForTurn == 0 || freeToPlayOnce)) {
							tmp[i] += 3.0F;
							if (dtBaseDragonDamage != (int) tmp[i]) {
								isDTDragonDamageModified = true;
							}
						}

						for (AbstractPower p : dragon.powers) {
							tmp[i] = p.atDamageGive(tmp[i], damageTypeForTurn);
						}
						for (AbstractPower p : AbstractDungeon.player.powers) {
							if (playerPowerApplyToDragon.contains(p.ID)) {
								tmp[i] = p.atDamageGive(tmp[i], damageTypeForTurn);
							}
						}
						for (AbstractPower p : mo.powers) {
							tmp[i] = p.atDamageReceive(tmp[i], this.damageTypeForTurn, this);
						}
					}

					for (i = 0; i < tmp.length; ++i) {
						for (AbstractPower p : dragon.powers) {
							tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
						}
						for (AbstractPower p : AbstractDungeon.player.powers) {
							if (playerPowerApplyToDragon.contains(p.ID)) {
								tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
							}
						}
						for (AbstractPower p : mo.powers) {
							tmp[i] = p.atDamageFinalReceive(tmp[i], this.damageTypeForTurn, this);
						}
					}

					for (i = 0; i < tmp.length; ++i) {
						if (tmp[i] < 0.0F) {
							tmp[i] = 0.0F;
						}
					}

					multiDamage = new int[tmp.length];

					for (i = 0; i < tmp.length; ++i) {
						multiDamage[i] = MathUtils.floor(tmp[i]);
					}

					dtDragonDamage = multiDamage[0];
					if (dtBaseDragonDamage != dtDragonDamage) {
						isDTDragonDamageModified = true;
					}
				}
			}
		}
	}

	public void upgradeDTDragonDamage(int amount) {
		dtBaseDragonDamage += amount;
		dtDragonDamage = dtBaseDragonDamage;
		upgradedDTDragonDamage = true;
	}

	public void upgradeDTDragonBlock(int amount) {
		dtBaseDragonBlock += amount;
		dtDragonBlock = dtBaseDragonBlock;
		upgradedDTDragonBlock = true;
	}

	public Dragon getDragon() {
		if (AbstractDungeon.player instanceof TheDT) {
			Dragon dragon = ((TheDT) AbstractDungeon.player).dragon;
			if (dragon.isDeadOrEscaped()) return null;
			return dragon;
		}
		return null;
	}

	public String dragonNotAvailableMessage() {
		if (AbstractDungeon.player instanceof TheDT) {
			return EXTENDED_DESCRIPTION[1];
		}
		return EXTENDED_DESCRIPTION[0];
	}

	// null = both attacks
	public AbstractCreature getAttacker() {
		if (AbstractDungeon.player instanceof TheDT) {
			if (this.dtCardTarget == DTCardTarget.DEFAULT) {
				return AbstractDungeon.player;
			} else if (this.dtCardTarget == DTCardTarget.DRAGON_ONLY) {
				return ((TheDT) AbstractDungeon.player).dragon;
			} else {
				return null;
			}
		} else {
			return AbstractDungeon.player;
		}
	}
}
