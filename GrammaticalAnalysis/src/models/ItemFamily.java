package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/17
 */
public class ItemFamily {
	private List<ItemSet> itemSets = new ArrayList<>();
	private Map<Integer, Map<String, Integer>> gotoMap = new HashMap<>();;

	public ItemFamily(Grammar grammar) {
		initFamily(grammar);
		createFamily(grammar);
	}

	private void initFamily(Grammar grammar) {
		String s = "Ω->" + grammar.getProduction(0).leftPart;
		Production augmentedGrammarProduction = new Production(s);
		Item item = new Item(augmentedGrammarProduction);
		ItemSet is = new ItemSet();
		is.initItemSet(item, grammar);
		itemSets.add(is);
	}

	private void createFamily(Grammar grammar) {
		boolean ALERTED_ = true;
		while(ALERTED_){
			Map<Integer, Map<String, Integer>> tmpGotoMap = new HashMap<>();
			ALERTED_ = false;
			for (int i = 0; i < itemSets.size(); i++) {
				List<String> caching = new ArrayList<>();
				ItemSet itemSet = itemSets.get(i);
				for(int index=0; index<itemSets.size(); index++){
					tmpGotoMap.put(index, new HashMap<>());
				}
				/* 遍历一遍第i个项目集，将所有出现在点后面的字符添加到缓存中 */
				for (int j = 0; j < itemSet.items.size(); j++) {
					Item item = itemSet.items.get(j);
					if (item.getPoint() >= item.getRightParts().size()) {
						// 如果·后面没有东西，就跳过这个项目的判断。
						continue;
					}
					if (!caching.contains(item.getRightParts().get(item.getPoint()))) {
						// 如果·后面有东西，将这个东西添加到caching缓存中。
						caching.add(item.getRightParts().get(item.getPoint()));
					}
				}

				/* 遍历缓存，表示逐个读入状态 */
				// jcache表示第j个可以读入的状态
				for (String jCache : caching) {
					ItemSet newIts = new ItemSet();
					for (int k = 0; k < itemSet.items.size(); k++) {
						List<String> rightParts = itemSet.items.get(k).rightParts;
						int point = itemSet.items.get(k).point;
						if (point < rightParts.size()) {
							if (rightParts.get(point).equals(jCache)) {
								// 如果点后面是有东西的，并且项目集中某个项目的point后的字符和缓存中的第k个字符相同。也就是E->·E-T读入字符E
								Item item = new Item(itemSet.items.get(k));
								newIts.initItemSet(item, grammar);
								tmpGotoMap.get(itemSet.id).put(jCache, newIts.id);
							}
						}
					}
					/* 如果新的状态集不存在于项目集规范族中，就将其添加到项目集规范族中 */
					int l;
					for (l = 0; l < itemSets.size(); l++) {
						//比较项目集的核以确定两个项目集是否相等
						if (newIts.equals(itemSets.get(l).items.get(0), newIts.items.get(0))) {
							break;
						}
					}
					if (l >= itemSets.size()) {
						itemSets.add(newIts);
						ALERTED_ = true;
					}else{
						tmpGotoMap.get(itemSet.id).put(jCache, l);
						newIts.updateID();
					}
				}
				gotoMap.computeIfAbsent(itemSet.id, k -> new HashMap<>());
				gotoMap.get(itemSet.id).putAll(tmpGotoMap.get(itemSet.id));
			}
		}

	}

	public void print() {
		System.out.println("\n该文法的项目集规范族如下所示：");
		for (int i = 0; i < itemSets.size(); i++) {
			System.out.println("I" + i + "：");
			for (int j = 0; j < itemSets.get(i).items.size(); j++) {
				StringBuilder s = new StringBuilder(itemSets.get(i).items.get(j).leftPart);
				s.append("->");
				for (int k = 0; k < itemSets.get(i).items.get(j).rightParts.size(); k++) {
					if (itemSets.get(i).items.get(j).point == k) {
						s.append("·");
					}
					s.append(itemSets.get(i).items.get(j).rightParts.get(k));
				}
				if (itemSets.get(i).items.get(j).point == itemSets.get(i).items.get(j).rightParts.size()) {
					s.append("·");
				}
				System.out.println(s);
			}
			System.out.println();
		}
	}

	public Map<Integer, Map<String, Integer>> getGotoMap() {
		return gotoMap;
	}

	public List<ItemSet> getItemSets() {
		return itemSets;
	}
}
