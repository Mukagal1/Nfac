import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BigramModel {

    private static Map<String, Integer> freq;
    private static List<String> names, continued;
    private static List<Character> firLetters;
    private static String a[][];
    private static int coOfBigrams, size;

    public static void main(String[] args) throws Exception {
        freq = new HashMap<>();
        firLetters = new ArrayList<>();
        names = new ArrayList<>();
        continued = new ArrayList<>();
        String filePath = "names.txt";
        nameGenerator(filePath);
        bigramFrequencies();
        size = freq.size();
        a = new String[freq.size()][2];
        Map<String, String> soutFq = getFreq();
        Random random = new Random();
        boolean visited[] = new boolean[size];

        int ind[] = new int[26];
        char c = ' ';
        Arrays.fill(ind, -1);
        for (int i = 0; i < freq.size(); i++) {
            if(a[i][0].charAt(0) != c){
                char p = a[i][0].charAt(0);
                ind[p - 'a'] = i;
                c = p;
            }
        }

        char firstLetter = firLetters.get(random.nextInt(firLetters.size()));
        System.out.println("Random first letter: " + firstLetter);

        Scanner in = new Scanner(System.in);

        String generatedName = generateName(firstLetter, ind, visited);
        System.out.println("Name: " + generatedName + "\n");
        System.out.print("Do you need frequencies of each bigrams? Yes or No: ");
        String yesOrNo = in.next();
        if(yesOrNo.equals("Yes"))
            System.out.println("\n" + soutFq);

    }

    static void nameGenerator(String filePath) throws Exception {
        FileReader fr = new FileReader(filePath);
        Scanner sc = new Scanner(fr);
        while (sc.hasNext()){
            String s = sc.next() + "$";
            names.add(s);
        }
        fr.close();
        sc.close();
    }

    static String generateName(char c, int arr[], boolean vis[]) {
        String name = c + "";

        while(c != '$'){
            c = getNext(c, arr[c - 'a'], vis);
            name += c;
        }

        return name.substring(0, name.length() - 1);
    }

    static char getNext(char c, int ind, boolean vis[]){
        if(ind == -1)
            return '&';
        char ans = '&';
        int index = -1;
        int min = Integer.MIN_VALUE;
        for (int i = ind; i < size; i++) {
            String name = a[i][0];
            if(vis[i])
                continue;
            int value = Integer.parseInt(a[i][1]);
            if(c != name.charAt(0))
                break;
            if(min < value){
                ans = name.charAt(1);
                min = value;
                index = i;
            }
        }
        if(index != -1)
            vis[index] = true;

        return ans;
    }

    static void bigramFrequencies() {
        int n = names.size();
        for (int i = 0; i < n; i++) {
            int size = names.get(i).length();
            String name = names.get(i);
            for (int j = 0; j < size - 1; j++) {
                coOfBigrams += 2;
                String s = name.substring(j, j + 2);
                if(!firLetters.contains(s.charAt(0)))
                    firLetters.add(s.charAt(0));
                if(!freq.containsKey(s))
                    freq.put(s, 1);
                else freq.put(s, freq.get(s) + 1);
            }
        }
    }

    static Map<String, String> getFreq(){
        Map<String, String> fq = new HashMap<>();
        int i = 0;
        char c = ' ';
        for (Map.Entry<String, Integer> m: freq.entrySet()) {
            String word = m.getKey();
            int value = m.getValue();
            a[i][0] = word;
            a[i][1] = String.valueOf(value);
            String frequency = value + "/" + coOfBigrams;
            fq.put(word, frequency);
            i++;
        }

        return fq;
    }
}
