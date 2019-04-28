package TheDT.relics;

import TheDT.DTMod;
import TheDT.characters.TheDT;
import TheDT.patches.LibraryTypeEnum;
import aspiration.Aspiration;
import aspiration.relics.skillbooks.SkillbookRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static TheDT.patches.CustomTags.DT_MYTHICAL;

public class MythicalSkillbook extends SkillbookRelic {

	private static final String RAW_ID = "MythicalSkillbook";
	public static final String ID = DTMod.makeID(RAW_ID);
	public static final String IMG = DTMod.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTMod.GetRelicOutlinePath(RAW_ID);

	public MythicalSkillbook() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.SPECIAL, LandingSound.MAGICAL, new ArrayList<>());
	}

	@Override
	public void onEquip() {
		modifyCardPool();
	}

	@Override
	public String getUpdatedDescription() {
		if (Aspiration.skillbookCardpool()) {
			return DESCRIPTIONS[0] + DESCRIPTIONS[1];
		} else {
			return DESCRIPTIONS[0];
		}
	}

	@Override
	public boolean canSpawn() {
		return !(AbstractDungeon.player instanceof TheDT) && !hasSkillbookRelic(AbstractDungeon.player);
	}

	public void modifyCardPool() {
		if (Aspiration.skillbookCardpool()) {
			DTMod.logger.info("Mythical Skillbook acquired, modifying card pool.");
			ArrayList<AbstractCard> classCards = CardLibrary.getCardList(LibraryTypeEnum.DT_ORANGE);
			mixCardpools(classCards);
		}
	}

	@Override
	protected void mixCardpools(ArrayList<AbstractCard> cardList) {
		for (AbstractCard c : cardList) {
			if (c.rarity != AbstractCard.CardRarity.BASIC && !c.hasTag(DT_MYTHICAL)) {
				switch (c.rarity) {
					case COMMON: {
						AbstractDungeon.commonCardPool.removeCard(c);
						AbstractDungeon.srcCommonCardPool.removeCard(c);
						AbstractDungeon.commonCardPool.addToTop(c);
						AbstractDungeon.srcCommonCardPool.addToBottom(c);
						continue;
					}
					case UNCOMMON: {
						AbstractDungeon.uncommonCardPool.removeCard(c);
						AbstractDungeon.srcUncommonCardPool.removeCard(c);
						AbstractDungeon.uncommonCardPool.addToTop(c);
						AbstractDungeon.srcUncommonCardPool.addToBottom(c);
						continue;
					}
					case RARE: {
						AbstractDungeon.rareCardPool.removeCard(c);
						AbstractDungeon.srcRareCardPool.removeCard(c);
						AbstractDungeon.rareCardPool.addToTop(c);
						AbstractDungeon.srcRareCardPool.addToBottom(c);
						continue;
					}
					case CURSE: {
						AbstractDungeon.curseCardPool.removeCard(c);
						AbstractDungeon.srcCurseCardPool.removeCard(c);
						AbstractDungeon.curseCardPool.addToTop(c);
						AbstractDungeon.srcCurseCardPool.addToBottom(c);
					}
				}
			}
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MythicalSkillbook();
	}
}
