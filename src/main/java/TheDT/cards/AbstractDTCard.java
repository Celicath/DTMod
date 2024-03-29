package TheDT.cards;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.powers.NewVigorPower;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.StrikeDummy;
import com.megacrit.cardcrawl.relics.WristBlade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static TheDT.patches.CustomTags.DT_TACTIC;

public abstract class AbstractDTCard extends CustomCard {
	public enum DTCardUser {
		YOU, DRAGON, FRONT, REAR, BOTH
	}

	private static final String DT_RAW_ID = "AbstractDTCard";
	public static final String DT_ID = DTModMain.makeID(DT_RAW_ID);
	private static final CardStrings dtCardStrings = CardCrawlGame.languagePack.getCardStrings(DT_ID);
	public static final String[] DT_CARD_EXTRA_TEXT = dtCardStrings.EXTENDED_DESCRIPTION;
	private static Texture dragonIconTexture = null;
	public static final int DRAGON_ICON_WIDTH = 128;
	public static final int DRAGON_ICON_HEIGHT = 128;

	protected final CardStrings cardStrings;
	protected final String NAME;
	protected final String DESCRIPTION;
	protected final String UPGRADE_DESCRIPTION;
	protected final String[] EXTENDED_DESCRIPTION;

	public int[] dragonMultiDamage;

	public static HashSet<String> playerPowerApplyToDragon;
	public static HashSet<String> relicApplyToDragon;

	static {
		playerPowerApplyToDragon = new HashSet<>();
		playerPowerApplyToDragon.add(VigorPower.POWER_ID);
		playerPowerApplyToDragon.add(NewVigorPower.POWER_ID);
		playerPowerApplyToDragon.add(PenNibPower.POWER_ID);
		relicApplyToDragon = new HashSet<>();
		relicApplyToDragon.add(WristBlade.ID);
		relicApplyToDragon.add(StrikeDummy.ID);
	}

	public int dtDragonDamage = -1;
	public int dtBaseDragonDamage = -1;
	public boolean upgradedDTDragonDamage = false;
	public boolean isDTDragonDamageModified = false;

	public int dtDragonBlock = -1;
	public int dtBaseDragonBlock = -1;
	public boolean upgradedDTDragonBlock = false;
	public boolean isDTDragonBlockModified = false;

	public DTCardUser dtCardUser;

	public AbstractDTCard(String rawId,
	                      int cost,
	                      CardType type,
	                      CardColor color,
	                      CardRarity rarity,
	                      CardTarget target,
	                      DTCardUser dtCardUser) {
		this(rawId, cost, type, color, rarity, target, rawId, dtCardUser);
	}

	public AbstractDTCard(String rawId,
	                      int cost,
	                      CardType type,
	                      CardColor color,
	                      CardRarity rarity,
	                      CardTarget target,
	                      String imgID,
	                      DTCardUser dtCardUser) {
		super(DTModMain.makeID(rawId), "NAME", DTModMain.GetCardPath(imgID), cost, "DESCRIPTION", type, color, rarity, target);

		this.dtCardUser = dtCardUser;

		cardStrings = CardCrawlGame.languagePack.getCardStrings(DTModMain.makeID(rawId));
		name = NAME = cardStrings.NAME;
		originalName = NAME;
		rawDescription = DESCRIPTION = cardStrings.DESCRIPTION;
		UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
		EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
		initializeTitle();
		initializeDescription();
	}

	public AbstractDTCard(String rawId,
	                      String name,
	                      CustomCard.RegionName img,
	                      int cost,
	                      String rawDescription,
	                      CardType type,
	                      CardColor color,
	                      CardRarity rarity,
	                      CardTarget target,
	                      DTCardUser dtCardUser) {
		super(DTModMain.makeID(rawId), name, img, cost, rawDescription, type, color, rarity, target);

		this.dtCardUser = dtCardUser;
		NAME = name;

		cardStrings = CardCrawlGame.languagePack.getCardStrings(DTModMain.makeID(rawId));
		DESCRIPTION = cardStrings.DESCRIPTION;
		UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
		EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
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
	protected void applyPowersToBlock() {
		super.applyPowersToBlock();

		Dragon dragon = DragonTamer.getLivingDragon();
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
	public void applyPowers() {
		super.applyPowers();
		Dragon dragon = DragonTamer.getLivingDragon();
		if (dtBaseDragonDamage != -1) {
			dtDragonDamage = dtBaseDragonDamage;
			if (dragon != null) {
				isDTDragonDamageModified = false;
				if (!isMultiDamage) {
					float tmp = (float) dtBaseDragonDamage;

					for (AbstractRelic r : AbstractDungeon.player.relics) {
						if (relicApplyToDragon.contains(r.relicId)) {
							tmp = r.atDamageModify(tmp, this);
						}
					}

					for (AbstractPower p : dragon.powers) {
						tmp = p.atDamageGive(tmp, damageTypeForTurn);
					}
					for (AbstractPower p : AbstractDungeon.player.powers) {
						if (playerPowerApplyToDragon.contains(p.ID)) {
							tmp = p.atDamageGive(tmp, damageTypeForTurn);
						}
					}
					tmp = dragon.stance.atDamageGive(tmp, damageTypeForTurn, this);
					for (AbstractPower p : dragon.powers) {
						tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
					}
					for (AbstractPower p : AbstractDungeon.player.powers) {
						if (playerPowerApplyToDragon.contains(p.ID)) {
							tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
						}
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
						for (AbstractRelic r : AbstractDungeon.player.relics) {
							if (relicApplyToDragon.contains(r.relicId)) {
								tmp[i] = r.atDamageModify(tmp[i], this);
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
						tmp[i] = dragon.stance.atDamageGive(tmp[i], damageTypeForTurn, this);
						for (AbstractPower p : dragon.powers) {
							tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
						}
						for (AbstractPower p : AbstractDungeon.player.powers) {
							if (playerPowerApplyToDragon.contains(p.ID)) {
								tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
							}
						}
					}

					for (i = 0; i < tmp.length; ++i) {
						if (tmp[i] < 0.0F) {
							tmp[i] = 0.0F;
						}
					}

					dragonMultiDamage = new int[tmp.length];

					for (i = 0; i < tmp.length; ++i) {
						dragonMultiDamage[i] = MathUtils.floor(tmp[i]);
					}

					dtDragonDamage = dragonMultiDamage[0];
					if (dtBaseDragonDamage != dtDragonDamage) {
						isDTDragonDamageModified = true;
					}
				}
			}
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);

		Dragon dragon = DragonTamer.getLivingDragon();
		if (dtBaseDragonDamage != -1) {
			dtDragonDamage = dtBaseDragonDamage;
			if (dragon != null) {
				isDTDragonDamageModified = false;
				if (!isMultiDamage && mo != null) {
					float tmp = (float) dtBaseDragonDamage;

					for (AbstractRelic r : AbstractDungeon.player.relics) {
						if (relicApplyToDragon.contains(r.relicId)) {
							tmp = r.atDamageModify(tmp, this);
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
					tmp = dragon.stance.atDamageGive(tmp, damageTypeForTurn, this);
					for (AbstractPower p : mo.powers) {
						tmp = p.atDamageReceive(tmp, damageTypeForTurn, this);
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
						tmp = p.atDamageFinalReceive(tmp, damageTypeForTurn, this);
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
						for (AbstractRelic r : AbstractDungeon.player.relics) {
							if (relicApplyToDragon.contains(r.relicId)) {
								tmp[i] = r.atDamageModify(tmp[i], this);
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
						tmp[i] = dragon.stance.atDamageGive(tmp[i], damageTypeForTurn, this);
						for (AbstractPower p : m.get(i).powers) {
							tmp[i] = p.atDamageReceive(tmp[i], damageTypeForTurn, this);
						}

						for (AbstractPower p : dragon.powers) {
							tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
						}
						for (AbstractPower p : AbstractDungeon.player.powers) {
							if (playerPowerApplyToDragon.contains(p.ID)) {
								tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
							}
						}
						for (AbstractPower p : m.get(i).powers) {
							tmp[i] = p.atDamageFinalReceive(tmp[i], damageTypeForTurn, this);
						}
					}

					for (i = 0; i < tmp.length; ++i) {
						if (tmp[i] < 0.0F) {
							tmp[i] = 0.0F;
						}
					}

					dragonMultiDamage = new int[tmp.length];

					for (i = 0; i < tmp.length; ++i) {
						dragonMultiDamage[i] = MathUtils.floor(tmp[i]);
					}

					dtDragonDamage = dragonMultiDamage[0];
				}
				if (dtBaseDragonDamage != dtDragonDamage) {
					isDTDragonDamageModified = true;
				}
			}
		}
	}

	public int calculateCardDamageAsMonster(AbstractCreature attacker, int[] baseDamage, AbstractMonster mo, int[] enemyMultiDamage) {
		if (!isMultiDamage && mo != null) {
			float tmp = baseDamage[0];

			for (AbstractRelic r : AbstractDungeon.player.relics) {
				if (relicApplyToDragon.contains(r.relicId)) {
					tmp = r.atDamageModify(tmp, this);
				}
			}

			for (AbstractPower p : attacker.powers) {
				tmp = p.atDamageGive(tmp, damageTypeForTurn);
			}
			for (AbstractPower p : AbstractDungeon.player.powers) {
				if (playerPowerApplyToDragon.contains(p.ID)) {
					tmp = p.atDamageGive(tmp, damageTypeForTurn);
				}
			}

			for (AbstractPower p : mo.powers) {
				tmp = p.atDamageReceive(tmp, damageTypeForTurn, this);
			}

			for (AbstractPower p : attacker.powers) {
				tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
			}
			for (AbstractPower p : AbstractDungeon.player.powers) {
				if (playerPowerApplyToDragon.contains(p.ID)) {
					tmp = p.atDamageFinalGive(tmp, damageTypeForTurn);
				}
			}
			for (AbstractPower p : mo.powers) {
				tmp = p.atDamageFinalReceive(tmp, damageTypeForTurn, this);
			}
			if (tmp < 0.0F) {
				tmp = 0.0F;
			}

			return MathUtils.floor(tmp);
		} else {
			ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
			float[] tmp = new float[m.size()];

			int i;
			for (i = 0; i < tmp.length; ++i) {
				tmp[i] = baseDamage[0];
			}

			for (i = 0; i < tmp.length; ++i) {
				for (AbstractRelic r : AbstractDungeon.player.relics) {
					if (relicApplyToDragon.contains(r.relicId)) {
						tmp[i] = r.atDamageModify(tmp[i], this);
					}
				}

				for (AbstractPower p : attacker.powers) {
					tmp[i] = p.atDamageGive(tmp[i], damageTypeForTurn);
				}
				for (AbstractPower p : AbstractDungeon.player.powers) {
					if (playerPowerApplyToDragon.contains(p.ID)) {
						tmp[i] = p.atDamageGive(tmp[i], damageTypeForTurn);
					}
				}
				for (AbstractPower p : m.get(i).powers) {
					tmp[i] = p.atDamageReceive(tmp[i], damageTypeForTurn, this);
				}

				for (AbstractPower p : attacker.powers) {
					tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
				}
				for (AbstractPower p : AbstractDungeon.player.powers) {
					if (playerPowerApplyToDragon.contains(p.ID)) {
						tmp[i] = p.atDamageFinalGive(tmp[i], damageTypeForTurn);
					}
				}
				for (AbstractPower p : m.get(i).powers) {
					tmp[i] = p.atDamageFinalReceive(tmp[i], damageTypeForTurn, this);
				}
			}

			for (i = 0; i < tmp.length; ++i) {
				if (tmp[i] < 0.0F) {
					tmp[i] = 0.0F;
				}
			}

			if (enemyMultiDamage != null && enemyMultiDamage.length > 0) {

				for (i = 0; i < tmp.length && i < enemyMultiDamage.length; ++i) {
					enemyMultiDamage[i] = MathUtils.floor(tmp[i]);
				}

				return enemyMultiDamage[0];
			} else {
				return (int) tmp[0];
			}
		}
	}

	public void upgradeDTDragonDamage(int amount) {
		dtBaseDragonDamage += amount;
		upgradedDTDragonDamage = true;
	}

	public void upgradeDTDragonBlock(int amount) {
		dtBaseDragonBlock += amount;
		upgradedDTDragonBlock = true;
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard card = super.makeStatEquivalentCopy();
		if (card instanceof AbstractDTCard) {
			AbstractDTCard dtCard = (AbstractDTCard) card;
			dtCard.dtBaseDragonDamage = dtBaseDragonDamage;
			dtCard.dtBaseDragonBlock = dtBaseDragonBlock;
		}
		return card;
	}

	@Override
	public void resetAttributes() {
		super.resetAttributes();
		isDTDragonBlockModified = false;
		isDTDragonDamageModified = false;
		dtDragonBlock = dtBaseDragonBlock;
		dtDragonDamage = dtBaseDragonDamage;
	}

	@Override
	public List<String> getCardDescriptors() {
		if (DT_CARD_EXTRA_TEXT.length > 2 && tags.contains(DT_TACTIC)) {
			return Collections.singletonList(DT_CARD_EXTRA_TEXT[2]);
		} else {
			return super.getCardDescriptors();
		}
	}

	public String dragonNotAvailableMessage() {
		if (AbstractDungeon.player instanceof DragonTamer) {
			return DT_CARD_EXTRA_TEXT[1];
		}
		return DT_CARD_EXTRA_TEXT[0];
	}

	public static Texture getDragonIconTexture() {
		if (dragonIconTexture == null) {
			dragonIconTexture = new Texture(DTModMain.makePath("ui/DragonIcon.png"));
		}
		return dragonIconTexture;
	}
}
