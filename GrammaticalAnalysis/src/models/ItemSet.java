package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/17
 */
public class ItemSet {
	private static int ID = 0;
	int id;
	List<Item> items = new ArrayList<>();

	public ItemSet() {
		this.id = ID++;
	}

	public void initItemSet(Item item, Grammar grammar) {
		boolean ALTERED_ = true;
		int i = items.size();
		items.add(item);
		List<String> nonTerminals = grammar.getNonTerminals();
		while(ALTERED_){
			if (item.point >= item.rightParts.size()) {
				break;
			}
			ALTERED_ = false;
			int itemsSize = items.size();
			for (; i < itemsSize; i++) {
				Item iitem = items.get(i);
				if(iitem.getRightParts().size()==0) {
					continue;
				}
				String pointToken = iitem.getRightParts().get(iitem.getPoint());
				if (nonTerminals.contains(pointToken)) {
					for(Production production : grammar.getProductionsMap().get(pointToken)){
						Item newItem = new Item(production);
						if(!contains(newItem)){
							items.add(newItem);
							ALTERED_ = true;
						}
					}
				}
			}
		}
	}

	private boolean contains(Item item) {
		// List自带的contains好像不可以用，判断items中是否有item
		int i = 0;
		for (; i < items.size(); i++) {
			// 判断第i个元素和传入的元素是否相同
			if (equals(items.get(i), item)) {
				break;
			}
		}
		return i < items.size();
	}

	public boolean equals(Item item1, Item item2) {
		// 判断item1和item2是否相同
		boolean judge = false;
		if (item1.rightParts.size() == item2.getRightParts().size()) {
			int j = 0;
			for (; j < item1.rightParts.size(); j++) {
				if (!item1.getRightParts().get(j).equals(item2.getRightParts().get(j))) {
					break;
				}
			}
			if (j >= item1.rightParts.size()) {
				// 右部相等
				judge = true;
			}
		}
		// 如果右部相同，且左部和点的位置都相同，那么说明就是一样的
		return judge && item1.leftPart.equals(item2.leftPart) && item1.point == item2.point;
	}

	public void updateID(){
		ItemSet.ID--;
	}

}
