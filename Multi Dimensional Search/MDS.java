/** Starter code for MDS
 *  @author rbk
 */

/**
 *
 */

package sxk180037;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class MDS {
    // Add fields of MDS here
    class ItemDetails {
        Money price;
        ArrayList<Long> description;

        ItemDetails() { }

        ItemDetails(Money newPrice, List<Long> newDescription) {
            this.price = new Money(newPrice.toString());
            this.description = new ArrayList<>(newDescription);
        }
    }

    TreeMap<Long, ItemDetails> itemIDPDMap;
    HashMap<Long, TreeSet<Long>> descItemsMap;
    // Constructors
    public MDS() {
        itemIDPDMap = new TreeMap<>();
        descItemsMap = new HashMap<>();
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated.
       Returns 1 if the item is new, and 0 otherwise.
    */
    public int insert(long id, Money price, java.util.List<Long> list) {
        ItemDetails value = null;
        int retVal = 1;
        if(!itemIDPDMap.isEmpty() && itemIDPDMap.containsKey(id) && itemIDPDMap.get(id) != null) {
            value = itemIDPDMap.get(id);
            if(list.size()!=0) {
                ArrayList<Long> list1 = value.description;
                for(long i: list1) {
                    TreeSet<Long> ts = descItemsMap.get(i);
                    if(ts.size()!=0 && ts.contains(id) && !ts.isEmpty())
                        ts.remove(id);
                    if(ts.size()==0)
                        descItemsMap.remove(i);
                    else
                        descItemsMap.replace(i, ts);
                }
                ArrayList<Long> newList = new ArrayList<>();
                //value.description = new ArrayList(list);
                for(long i: list) {
                    TreeSet<Long> ts;
                    if(descItemsMap.containsKey(i))
                        ts = descItemsMap.get(i);
                    else
                        ts = new TreeSet<Long>();
                    ts.add(id);
                    descItemsMap.put(i, ts);
                    if(!newList.contains(i))
                        newList.add(i);
                }
                value.description = newList;
            }
            value.price = new Money(price.toString());
            itemIDPDMap.put(id, value);
            retVal= 0;
        }
        else {
            value = new ItemDetails();
            value.price = new Money(price.toString());
            ArrayList<Long> newList = new ArrayList<>();
            for(long i: list) {
                if(!newList.contains(i))
                    newList.add(i);
            }
            value.description = newList;
            itemIDPDMap.put(id, value);

            for(long i:list) {
                TreeSet<Long> ts;
                if(descItemsMap.containsKey(i)) {
                    ts = descItemsMap.get(i);
                }
                else {
                    ts = new TreeSet<>();
                }
                ts.add(id);
                descItemsMap.put(i, ts);
            }
        }

        System.out.println("Insert Return = "+retVal);
        return retVal;
    }
        //return retVal;

    // b. Find(id): return price of item with given id (or 0, if not found).
    public Money find(long id) {
        if(!itemIDPDMap.containsKey(id)) {
            System.out.println("Find Return = " + 0);
            return new Money();
        }
        else {
            Money price = itemIDPDMap.get(id).price;
            System.out.println("Find Return = "+price.toString());
            return price;
        }
    }

    /*
       c. Delete(id): delete item from storage.  Returns the sum of the
       long ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */
    public long delete(long id) {
        if(!itemIDPDMap.containsKey(id)) {
            System.out.println("Delete Return = "+0);
            return 0;
        }
        else {
            ItemDetails value = itemIDPDMap.get(id);
            ArrayList<Long> list1 = value.description;
            long sum = 0;
            for(long i : list1) {
                TreeSet<Long> ts = descItemsMap.get(i);
                if(ts.contains(id))
                    ts.remove(id);
                if(ts.size()==0)
                    descItemsMap.remove(i);
                else
                    descItemsMap.put(i, ts);
                sum+=i;
            }
            itemIDPDMap.remove(id);
            System.out.println("Delete Return = "+sum+" Term: "+id);
            return sum;
        }
    }

    /*
       d. FindMinPrice(n): given a long int, find items whose description
       contains that number (exact match with one of the long ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */
    public Money findMinPrice(long n) {
        if(!descItemsMap.containsKey(n)) {
            System.out.println("MinPrice Return = "+0);
            return new Money();
        }

        TreeSet ts = (TreeSet) descItemsMap.get(n);

        long minID = (long) ts.first();
        System.out.println("FirstMinID = "+minID);

        Money minPrice = itemIDPDMap.get(minID).price;
        System.out.println("FirstMinPrice = "+minPrice.toString());
        Iterator<Long> iterator = ts.iterator();
        while(iterator.hasNext()) {
            long id = iterator.next();
            Money price = itemIDPDMap.get(id).price;
            System.out.println("Considering ID: "+id+" with Price: "+price.toString());
            if(minPrice.compareTo(price) == 1) {
                minPrice = new Money(price.toString());
            }
        }
        System.out.println("MinPrice Return = "+minPrice.toString());
        return minPrice;
    }

    /*
       e. FindMaxPrice(n): given a long int, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */
    public Money findMaxPrice(long n) {
        if(!descItemsMap.containsKey(n)) {
            System.out.println("Maxprice Return = "+0);
            return new Money();
        }

        TreeSet ts = (TreeSet) descItemsMap.get(n);

        long maxID = (long) ts.first();
        System.out.println("FirstMaxID = "+maxID);
        Money maxPrice = itemIDPDMap.get(maxID).price;
        System.out.println("FirstMaxPrice = "+maxPrice.toString());
        Iterator<Long> iterator = ts.iterator();
        while(iterator.hasNext()) {
            long id = iterator.next();
            System.out.println("Considering ID: "+id);
            Money price = itemIDPDMap.get(id).price;
            System.out.println("With price: "+price.toString());
            if(price.compareTo(maxPrice) == 1) {
                maxPrice = new Money(price.toString());
            }
        }
        System.out.println("MaxPrice Return = "+maxPrice.toString());
        return maxPrice;
    }

    /*
       f. FindPriceRange(n,low,high): given a long int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    public int findPriceRange(long n, Money low, Money high) {
        if(!descItemsMap.containsKey(n)) {
            System.out.println("findPriceRange Return = "+0);
            return 0;
        }

        int number = 0;
        TreeSet<Long> ts = (TreeSet<Long>) descItemsMap.get(n);
        for(Long i: ts) {
            Money price = itemIDPDMap.get(i).price;
            if((price.compareTo(low) == 1 || price.equals(low))  && (high.compareTo(price) == 1 || price.equals(high)) )
                number++;
        }
        System.out.println("findPriceRange Return = "+number);
        return number;
    }

    /*
       g. PriceHike(l,h,r): increase the price of every product, whose id is
       in the range [l,h] by r%.  Discard any fractional pennies in the new
       prices of items.  Returns the sum of the net increases of the prices.
    */
    public Money priceHike(long l, long h, double rate) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        SortedMap<Long, ItemDetails> filteredMap = itemIDPDMap.subMap(l-1, h+1);
        //Set<Long> hs = itemIDPDMap.keySet();
        double sum = 0;
        for(Map.Entry<Long,ItemDetails> entry : filteredMap.entrySet()) {
            long i = entry.getKey();
            ItemDetails value = itemIDPDMap.get(i);
            Money price = value.price;
               /*String oldPrice = price.toString();
                Double oldAmount = Double.parseDouble(oldPrice);
                Double newAmount = */
                //long d = price.d;
                //int c = price.c;
            double oldAmount = Double.parseDouble(price.toString());
            double newAmount = oldAmount * (100+rate)/100.0;
            String newPrice = df.format(newAmount);
            sum += newAmount-oldAmount;
               //price = new Money(newPrice);
            value.price = new Money(newPrice);
            itemIDPDMap.replace(i, value);
        }
        System.out.println("PriceHike Return = "+df.format(sum));
        return new Money(df.format(sum));
    }

    /*
      h. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    public long removeNames(long id, java.util.List<Long> list) {
        long sum = 0;
        ItemDetails value = itemIDPDMap.get(id);
        List<Long> list1 = value.description;
        for(long i: list) {
            if(list1.contains(i)) {
                sum += i;
                list1.remove(i);
                TreeSet<Long> ts = descItemsMap.get(i);
                ts.remove(id);
                if(ts.isEmpty()) {
                    descItemsMap.remove(i);
                }
                else {
                    descItemsMap.put(i, ts);
                }
            }
        }
        value.description = new ArrayList<>(list1);
        itemIDPDMap.put(id, value);
        System.out.println("removeNames Return = "+sum);
        return sum;
    }

    // Do not modify the Money class in a way that breaks LP3Driver.java
    public static class Money implements Comparable<Money> {
        long d;  int c;
        public Money() { d = 0; c = 0; }
        public Money(long d, int c) { this.d = d; this.c = c; }
        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if(len < 1) { d = 0; c = 0; }
            else if(part.length == 1) { d = Long.parseLong(s);  c = 0; }
            else { d = Long.parseLong(part[0]);  c = Integer.parseInt(part[1]); }
        }
        public long dollars() { return d; }
        public int cents() { return c; }
        public int compareTo(Money other) { // Complete this, if needed
            if(this.d > other.d)
                return 1;
            else if (this.d < other.d)
                return 0;
            else {
                if(this.c > other.c)
                    return 1;
                else
                    return 0;
            }
        }
        public boolean equals(Money other) {
            if(this.d == other.d && this.c == other.c)
                return true;
            else
                return false;
        }
        public String toString() {
            String cents = "0";
            if(c<10) {
                cents = cents + c;
                return d + "." + cents;
            }
            return d + "." + c;
        }
    }

}

