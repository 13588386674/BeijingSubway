package SubwayResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static ArrayList<Line> LineList = new ArrayList<>();//���������·���б�
    static ArrayList<Station> StationList = new ArrayList<>();//�����·վ����б�
    static HashMap<String, Station> stationHashMap = new HashMap<>();//��Ŷ�Ӧվ���Hash
    /*���ļ����ж���ʹ洢����*/
    public static void SubwayMessage(String pathname){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(pathname)));
            String NowLine = null;
            while ((NowLine=bufferedReader.readLine()) != null){
                Line line = new Line();
                String[] StationInformation = NowLine.split(" ");
                line.setLineName(StationInformation[0]);
                for (int i = 1; i < StationInformation.length-1; i++){
                    Station NowStation = new Station();
                    Station NextStation = new Station();
                    if(stationHashMap.containsKey(StationInformation[i])){
                        /*���hashmap���Ѿ����ڸ�վ����Ϣ����Ϊ��Ҫ�޸����Խ�������NowStation��*/
                       NowStation = stationHashMap.get(StationInformation[i]);
                       stationHashMap.remove(StationInformation[i]);
                    }
                    else{
                        NowStation.setStationName(StationInformation[i]);
                        NowStation.setVisited(false);
                    }
                    if(stationHashMap.containsKey(StationInformation[i+1])){
                        /*���hashmap���Ѿ����ڸ�վ����Ϣ����Ϊ��Ҫ�޸����Խ�������NowStation��*/
                        NextStation = stationHashMap.get(StationInformation[i+1]);
                        stationHashMap.remove(StationInformation[i+1]);
                    }
                    else{
                        NextStation.setStationName(StationInformation[i+1]);
                        NextStation.setVisited(false);
                    }
                    /*���վ�㲻������ǰ��·������·�����վ��*/
                    if(!NowStation.getStationLine().contains(line.LineName)){
                        NowStation.getStationLine().add(line.LineName);
                    }
                    if(!NextStation.getStationLine().contains(line.LineName)){
                        NextStation.getStationLine().add(line.LineName);
                    }
                    /*���վ�㲻����һվ��������վ�����NextStation*/
                    if(!NowStation.getNearStation().contains(NextStation)){
                        NowStation.getNearStation().add(NextStation);
                    }
                    if(!NextStation.getNearStation().contains(NowStation)){
                        NextStation.getNearStation().add(NowStation);
                    }
                    NowStation.setPreStation(NowStation.getStationName());
                    NextStation.setPreStation(NextStation.getStationName());

                    stationHashMap.put(StationInformation[i], NowStation);
                    stationHashMap.put(StationInformation[i+1], NextStation);
                    /*����ǰվ���������·��*/
                    if(!line.getStation().contains(NowStation.getStationName())){
                        line.getStation().add(NowStation.getStationName());
                    }
                    if(!line.getStation().contains(NextStation.getStationName())){
                        line.getStation().add(NextStation.getStationName());
                    }
                }
                LineList.add(line);
            }
            /*�ͷ���Դ*/
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Read file error, Please try again!");
        }
    }
    /*�����·��Ϣ*/
    public static void PrintMessage(String StartStation, String EndStation){
        List<String> list = new ArrayList<>();
        String NowStation = EndStation;
        String PreLine = "";
        while (!NowStation.equals(StartStation)){
            list.add(NowStation);
            NowStation = stationHashMap.get(NowStation).getPreStation();
        }
        Collections.reverse(list);
        System.out.println("��ǰ·�̹�������"+(list.size())+"վ");
        System.out.print(StartStation);
        for (int i = 0; i < list.size(); i++){
            if(stationHashMap.get(list.get(i)).getStationLine().size()==1){
                /*�����ǰվ��ֻ����һ����·����Ȼ�������ڴ�վ����*/
                System.out.print("-->"+stationHashMap.get(list.get(i)).getStationName());
                PreLine = stationHashMap.get(list.get(i)).getStationLine().get(0);
            }
            else {
                /*�����վ����ڶ�����·��������һվֻ��һ����·*/
                if (stationHashMap.get(list.get(i+1)).getStationLine().size()==1){
                    /*�����վ��ǰһվ��·��ͬ��ֱ�����
                    * �����ͬ�������Ϊ�����ߣ������վ����Ȼ����ʾ������·��Ϣ*/
                    if(stationHashMap.get(list.get(i+1)).getStationLine().get(0).equals(PreLine)){
                        System.out.print("-->"+stationHashMap.get(list.get(i)).getStationName());
                    }
                    else{
                        System.out.println("-->"+stationHashMap.get(list.get(i)).getStationName());
                        System.out.println("����"+stationHashMap.get(list.get(i+1)).getStationLine().get(0));
                        PreLine = stationHashMap.get(list.get(i+1)).getStationLine().get(0);
                    }
                }
                else{
                    /*���ڶ���·վ�㣬�������ǰһվ����·��Ϣ˵������Ҫ����
                    * ���������������ǰ����վ����·��Ϣ��������·һ����������վ�����е���·�ڵ���*/
                    if (stationHashMap.get(list.get(i+1)).getStationLine().contains(PreLine)){
                        System.out.print("-->"+stationHashMap.get(list.get(i)).getStationName());
                    }
                    else{
                        boolean IsSame = false;
                        for (int t1 = 0; t1 < stationHashMap.get(list.get(i)).getStationLine().size(); t1++){
                            if (stationHashMap.get(list.get(i+1)).getStationLine().contains(stationHashMap.get(list.get(i)).getStationLine().get(t1))){
                                System.out.println("-->"+stationHashMap.get(list.get(i)).getStationName());
                                System.out.println("����"+stationHashMap.get(list.get(i)).getStationLine().get(t1));
                                PreLine = stationHashMap.get(list.get(i)).getStationLine().get(t1);
                                IsSame = true;
                                break;
                            }
                        }
                        if(IsSame){
                            System.out.print("-->"+stationHashMap.get(list.get(i)).getStationName());
                        }
                    }
                }
            }
        }
    }
    /*����BFS�㷨�������·������*/
    public static void SearchShortPath(String StartStation, String EndStation){
        /*��ʼ������վ��ı�����Ϣ*/
        for (Map.Entry<String, Station> entry: stationHashMap.entrySet()){
            entry.getValue().setVisited(false);
        }
        Queue<String> queue = new LinkedList<>();
        queue.add(StartStation);
        while (!queue.isEmpty()){
            /*������в�Ϊ�գ��Ƴ�����ͷ��������վ����Ϊ������*/
            String NowStation = queue.poll();
            stationHashMap.get(NowStation).setVisited(true);
            if (NowStation.equals(EndStation)){
                break;
            }
            /*���ڸ�վ����������ڽڵ���б��������δ����������PreStation����Ϊ��ǰվ����������վ�����������*/
            for (Station station:stationHashMap.get(NowStation).NearStation){
                if (stationHashMap.get(station.getStationName()).isVisited()==false){
                    stationHashMap.get(station.getStationName()).setPreStation(NowStation);
                    queue.add(station.getStationName());
                }
            }
        }
    }
    /*������·��Ϣ*/
    public static void SearchLine(String LineName){
        List<String> Line = new ArrayList<>();
        boolean IsSearch =false;
        for(Line line: LineList){
            if (line.getLineName().equals(LineName)){
                IsSearch = true;
                System.out.println("��ǰ��·Ϊ"+LineName);
                for (int i = 0; i < line.getStation().size(); i++){
                    if (i == 0){
                        System.out.print(line.getStation().get(i));
                    }else{
                        System.out.print("-->"+line.getStation().get(i));
                    }
                }
                break;
            }
        }
        if(IsSearch == false){
            System.out.println("δ�ҵ�����·");
        }
    }
    public static void main(String[] args) {
        SubwayMessage("E:\\StudyofProject\\IDEAProjects\\Introduction to Software Engineering\\src\\SubwayResult\\Subway.txt");
        Scanner input = new Scanner(System.in);
        System.out.println("������·�˵�" );
        System.out.println("һ�������ѯ������·��Ϣ���밴 1");
        System.out.println("���������ѯ�������·�����밴 2");
        String op = input.nextLine();
        if(op.equals("1")){
            System.out.println("��������·��");
            String LineName = input.nextLine();
            SearchLine(LineName);
        }
        if(op.equals("2")) {
            System.out.println("��������ʼվ��");
            String start = input.nextLine();
            System.out.println("�������յ�վ");
            String end = input.nextLine();
            boolean IsExisted = true;
            if (!stationHashMap.containsKey(start)) {
                IsExisted = false;
                System.out.println("�����ڸ���ʼվ");

            }
            if (!stationHashMap.containsKey(end)) {
                IsExisted = false;
                System.out.println("�����ڸ��յ�վ");
            }
            if (IsExisted) {
                SearchShortPath(start, end);
                PrintMessage(start, end);
            }
        }
    }

}
