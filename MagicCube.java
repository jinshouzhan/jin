package com.test.myclass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
*author: jinshouzhan
*email: jsz.2008@163.com
*qq: 330580
*just for interest
*/
public class MagicCube implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int num=3;	//阶数，默认3阶
	//块数组,由高到低维分别是层（由下到上）、行（从后到前）、列（从左到右）
	//例如blocks[0][1][2]为由下到上第一层，从后到前第二行，从左到右第三列
	private Block[][][] blocks; 
	public Block[][][] getBlocks() {
		if(blocks==null){
			return new Block[num][num][num];
		}
		return blocks;
	}
	public void setBlocks(Block[][][] blocks) throws Exception {
		if(blocks.length==num&&blocks[0].length==num&&blocks[0][0].length==num){
			this.blocks = blocks;
		}else{
			throw new Exception("请检查Block数组阶数！");
		}

	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	//块类
	class Block implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public Block(){}
		public Block(BlockType type,Aspect[] aspects){
			this.type=type;
			this.aspects=aspects;
		}
		
		private BlockType type;//块类型
		private Aspect[] aspects;//块的各面
		public BlockType getType() {
			return type;
		}
		public void setType(BlockType type) {
			this.type = type;
		}
		public Aspect[] getAspects() {
			return aspects;
		}
		public void setAspects(Aspect[] aspects) {
			this.aspects = aspects;
		}
		@Override
		public int hashCode() {
			int count=0;
			if(aspects!=null){
				for(Aspect aspect:aspects){
					count+=(aspect==null?0:aspect.hashCode())*31;
				}
			}
			return count+(type==null?0:type.name().hashCode());
		}
		@Override
		public boolean equals(Object obj) {
			if(obj==null){
				return false;
			}
	        if (!(this.getClass()== obj.getClass())) {
	            return false;
	        }
	        Block objbl=(Block)obj;
	        if(this.getType()==objbl.getType()){
	        	Aspect[] aspss=this.getAspects();
	        	Aspect[] aspso=objbl.getAspects();
	        	if(aspss==null&&aspso==null){
	        		return true;
	        	}else if(aspss==null){
	        		return false;
	        	}else if(aspso==null){
	        		return false;
	        	}else if(aspss.length!=aspso.length){
	        		return false;
	        	}else{
	        		for(int i=0;i<aspss.length;i++){
	        			if(!aspss[i].equals(aspso[i])){
	        				return false;
	        			}
	        		}
	        		return true;
	        	}
	        }
	        return false;
		}		
	}
	//快类型枚举
	enum BlockType{
		ZEROFACE,ONEFACE,TWOFACES,THREEFACES
	}
	//块面类
	class Aspect implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public Aspect(){}
		public Aspect(String color,Pole pole){
			this.color=color;
			this.pole=pole;
		}
		
		private String color;	//颜色
		private Pole pole;		//块面朝向

		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public Pole getPole() {
			return pole;
		}
		public void setPole(Pole pole) {
			this.pole = pole;
		}
		@Override
		public int hashCode() {
			return (color==null?0:color.hashCode())*31+(pole==null?0:pole.name().hashCode());
		}
		@Override
		public boolean equals(Object obj) {
			if(obj==null){
				return false;
			}
	        if (!(this.getClass()== obj.getClass())) {
	            return false;
	        }
	        Aspect objas=(Aspect)obj;
	        if(this.getPole()==objas.getPole()){
	        	if(this.getColor()==null&&objas.getColor()==null){
	        		return true;
	        	}else if(this.getColor()==null){
	        		return false;
	        	}else if(objas.getColor()==null){
	        		return false;
	        	}else if(this.getColor().equals(objas.getColor())){
					return true;
				}
	        }
			return false;
		}
	}
	//块面朝向枚举
	enum Pole{
		FRONT,BACK,UP,DOWN,LEFT,RIGHT
	}
	//转动操作枚举
	enum Operation{
		FRONTCLOCKWISE,ANTIFRONTCLOCKWISE,BACKCLOCKWISE,ANTIBACKCLOCKWISE,
		UPCLOCKWISE,ANTIUPCLOCKWISE,DOWNCLOCKWISE,ANTIDOWNCLOCKWISE,
		LEFTCLOCKWISE,ANTILEFTCLOCKWISE,RIGHTCLOCKWISE,ANTIRIGHTCLOCKWISE
	}
	/*
	 * 判断块组等价
	 */
	public static boolean judgeBocksEqual(Block[][][] blocks1,Block[][][] blocks2){
		if(blocks1==null&&blocks2==null){
			return true;
		}else if(blocks1==null){
			return false;
		}else if(blocks2==null){
			return false;
		}
		if(blocks1.length!=blocks2.length){
			return false;
		}
		for(int i=0;i<blocks1.length;i++){
			if(blocks1[i]==null&&blocks2[i]==null){
				continue;
			}else if(blocks1[i]==null){
				return false;
			}else if(blocks2[i]==null){
				return false;
			}
			if(blocks1[i].length!=blocks2[i].length){
				return false;
			}
			for(int j=0;j<blocks1[i].length;j++){
				if(blocks1[i][j]==null&&blocks2[i][j]==null){
					continue;
				}else if(blocks1[i][j]==null){
					return false;
				}else if(blocks2[i][j]==null){
					return false;
				}
				if(blocks1[i][j].length!=blocks2[i][j].length){
					return false;
				}
				for(int k=0;k<blocks1[i][j].length;k++){
					if(!blocks1[i][j][k].equals(blocks2[i][j][k])){
						return false;
					}
				}
			}
		}
		return true;
	}
	

	/*
	 * 	深度拷贝块数组
	 * 	(序列化和反序列化对枚举进行了特殊处理，保证处理前后,jvm中枚举值具有唯一性和安全性 )
	 */
	public static Block[][][] deepClone(Block[][][] blocks){
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		Block[][][] bls=null;
		try{
	        // 序列化
	        bos = new ByteArrayOutputStream();
	        oos = new ObjectOutputStream(bos);
	        oos.writeObject(blocks);
	        // 反序列化
	        bis = new ByteArrayInputStream(bos.toByteArray());
	        ois = new ObjectInputStream(bis);
	        bls=(Block[][][])ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bos!=null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bls;
	}	
	/*
	 * 操作模块按日常的转动层号（一个操作从外向里数,从1开始）
	 */
	public void operateCubeForOrdinaryIndex(Operation operation, int index) throws Exception{
		int operationIndex;
		int number=this.getNum();
		if(operation==Operation.FRONTCLOCKWISE||operation==Operation.ANTIFRONTCLOCKWISE
			||operation==Operation.UPCLOCKWISE||operation==Operation.ANTIUPCLOCKWISE
			||operation==Operation.RIGHTCLOCKWISE||operation==Operation.ANTIRIGHTCLOCKWISE){
			operationIndex=number-index;
		}else{
			operationIndex=index-1;
		}
		operateCube(operation, operationIndex);
	}
	//操作,operationIndex为操作层序号，由下到上或从后到前或从左到右，从0开始
	private void operateCube(Operation operation, int operationIndex) throws Exception{
		if(operation==null){
			throw new Exception("operation is null!");
		}
		int number=this.getNum();
		Block[][][] blocks = this.getBlocks();
		if(blocks==null){
			throw new Exception("magicCube's blocks is null!");
		}
		Block[][][] cloneBlocks=deepClone(blocks);
		if(operation==Operation.FRONTCLOCKWISE||operation==Operation.ANTIBACKCLOCKWISE){
			for(int i=0;i<number;i++){
				for(int j=0;j<number;j++){
					blocks[i][operationIndex][j]=cloneBlocks[j][operationIndex][number-1-i];
					judgeForReverseAspect(blocks[i][operationIndex][j],operation);
				}
			}
		}
		if(operation==Operation.BACKCLOCKWISE||operation==Operation.ANTIFRONTCLOCKWISE){
			for(int i=0;i<number;i++){
				for(int j=0;j<number;j++){
					blocks[i][operationIndex][j]=cloneBlocks[number-1-j][operationIndex][i];
					judgeForReverseAspect(blocks[i][operationIndex][j],operation);
				}
			}
		}		
		if(operation==Operation.UPCLOCKWISE||operation==Operation.ANTIDOWNCLOCKWISE){
			for(int i=0;i<number;i++){
				for(int j=0;j<number;j++){
					blocks[operationIndex][i][j]=cloneBlocks[operationIndex][number-1-j][i];
					judgeForReverseAspect(blocks[operationIndex][i][j],operation);
				}
			}
		}		
		if(operation==Operation.DOWNCLOCKWISE||operation==Operation.ANTIUPCLOCKWISE){
			for(int i=0;i<number;i++){
				for(int j=0;j<number;j++){
					blocks[operationIndex][i][j]=cloneBlocks[operationIndex][j][number-1-i];
					judgeForReverseAspect(blocks[operationIndex][i][j],operation);
				}
			}
		}
		if(operation==Operation.RIGHTCLOCKWISE||operation==Operation.ANTILEFTCLOCKWISE){
			for(int i=0;i<number;i++){
				for(int j=0;j<number;j++){
					blocks[i][j][operationIndex]=cloneBlocks[number-1-j][i][operationIndex];
					judgeForReverseAspect(blocks[i][j][operationIndex],operation);
				}
			}
		}		
		if(operation==Operation.LEFTCLOCKWISE||operation==Operation.ANTIRIGHTCLOCKWISE){
			for(int i=0;i<number;i++){
				for(int j=0;j<number;j++){
					blocks[i][j][operationIndex]=cloneBlocks[j][number-1-i][operationIndex];
					judgeForReverseAspect(blocks[i][j][operationIndex],operation);
				}
			}
		}
		cloneBlocks=null;
	}
	//判断操作执行块面旋转
	private void judgeForReverseAspect(Block block,Operation operation){
		if(block==null){
			return;
		}
		Aspect[] aspects = block.getAspects();
		if(aspects==null||aspects.length==0){
			return;
		}
		for(int i=0;i<aspects.length;i++){
			turnAspectPole(aspects[i],operation);
		}
	}
	//转换块面朝向
	private void turnAspectPole(Aspect aspect,Operation operation){
		if(aspect==null||operation==null){
			return;
		}
		if(operation==Operation.FRONTCLOCKWISE||operation==Operation.ANTIBACKCLOCKWISE){
			switch(aspect.getPole()){
			case UP:
				aspect.setPole(Pole.RIGHT);
				break;
			case RIGHT:
				aspect.setPole(Pole.DOWN);
				break;
			case DOWN:
				aspect.setPole(Pole.LEFT);
				break;					
			case LEFT:
				aspect.setPole(Pole.UP);
				break;					
			}			
		}
		if(operation==Operation.BACKCLOCKWISE||operation==Operation.ANTIFRONTCLOCKWISE){
			switch(aspect.getPole()){
			case UP:
				aspect.setPole(Pole.LEFT);
				break;
			case LEFT:
				aspect.setPole(Pole.DOWN);
				break;
			case DOWN:
				aspect.setPole(Pole.RIGHT);
				break;
			case RIGHT:
				aspect.setPole(Pole.UP);
				break;										
			}			
		}
		if(operation==Operation.UPCLOCKWISE||operation==Operation.ANTIDOWNCLOCKWISE){
			switch(aspect.getPole()){
			case BACK:
				aspect.setPole(Pole.RIGHT);
				break;
			case RIGHT:
				aspect.setPole(Pole.FRONT);
				break;
			case FRONT:
				aspect.setPole(Pole.LEFT);
				break;
			case LEFT:
				aspect.setPole(Pole.BACK);
				break;										
			}			
		}
		if(operation==Operation.DOWNCLOCKWISE||operation==Operation.ANTIUPCLOCKWISE){
			switch(aspect.getPole()){
			case FRONT:
				aspect.setPole(Pole.RIGHT);
				break;
			case RIGHT:
				aspect.setPole(Pole.BACK);
				break;
			case BACK:
				aspect.setPole(Pole.LEFT);
				break;
			case LEFT:
				aspect.setPole(Pole.FRONT);
				break;										
			}			
		}		
		if(operation==Operation.LEFTCLOCKWISE||operation==Operation.ANTIRIGHTCLOCKWISE){
			switch(aspect.getPole()){
			case UP:
				aspect.setPole(Pole.FRONT);
				break;
			case FRONT:
				aspect.setPole(Pole.DOWN);
				break;
			case DOWN:
				aspect.setPole(Pole.BACK);
				break;
			case BACK:
				aspect.setPole(Pole.UP);
				break;										
			}			
		}		
		if(operation==Operation.RIGHTCLOCKWISE||operation==Operation.ANTILEFTCLOCKWISE){
			switch(aspect.getPole()){
			case UP:
				aspect.setPole(Pole.BACK);
				break;
			case BACK:
				aspect.setPole(Pole.DOWN);
				break;
			case DOWN:
				aspect.setPole(Pole.FRONT);
				break;
			case FRONT:
				aspect.setPole(Pole.UP);
				break;										
			}			
		}		
	}
	/*
	 * 	返回json格式blocks状态
	 */
	public static String blocksToJsonString(Block[][][] blocks){
		if(blocks==null){
			return "null";
		}
		if(blocks.length==0){
			return "{}";
		}
		StringBuffer bf=new StringBuffer();
		bf.append("{");
		for(int i=0;i<blocks.length;i++){
			if(blocks[i]==null){
				continue;
			}
			for(int j=0;j<blocks[i].length;j++){
				if(blocks[i][j]==null){
					continue;
				}
				for(int k=0;k<blocks[i][j].length;k++){
					if(blocks[i][j][k]==null){
						bf.append("blocks_").append(i).append("_").append(j)
						.append("_").append(k).append(":")
						.append("null").append(",");
					}else{
						Aspect[] aspects = blocks[i][j][k].getAspects();
						BlockType type = blocks[i][j][k].getType();
						bf.append("blocks_").append(i).append("_").append(j)
						.append("_").append(k).append(":")
						.append("{").append("type").append(":");
						if(type==null){
							bf.append("null").append(",");
						}else{
							bf.append("'").append(type.name()).append("'").append(",");
						}
						bf.append("aspects").append(":");
						if(aspects==null){
							bf.append("null");
						}else{
							bf.append("[");
							for(int m=0;m<aspects.length;m++){
								if(aspects[m]==null){
									bf.append("null").append(",");
									continue;
								}
								bf.append("{");
								String color = aspects[m].getColor();
								Pole pole = aspects[m].getPole();
								bf.append("color").append(":");
								if(color==null){
									bf.append("null").append(",");
								}else{
									bf.append("'").append(color).append("'").append(",");
								}
								bf.append("pole").append(":");
								if(pole==null){
									bf.append("null").append(",");
								}else{
									bf.append("'").append(pole.name()).append("'");
								}
								bf.append("}").append(",");
							}
							if(bf.charAt(bf.length()-1)==','){
								int ld=bf.lastIndexOf(",");
								bf.deleteCharAt(ld);
							}
							bf.append("]");
						}
						bf.append("}");
						bf.append(",");
					}
				}
			}
		}
		if(bf.charAt(bf.length()-1)==','){
			int l=bf.lastIndexOf(",");
			bf.deleteCharAt(l);
		}
		bf.append("}");
		return bf.toString();
	} 
	/*
	 * 	构造一个全部归位的3阶块组
	 */
	public Block[][][] initBlocks3ForSample(){
		Block[][][] blocks=new Block[3][3][3];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				for(int k=0;k<3;k++){
					blocks[i][j][k]=new Block();
					if((i==0||i==2)&&(j==0||j==2)&&(k==0||k==2)){
						blocks[i][j][k].setType(BlockType.THREEFACES);
					}else if((i==1&&j!=1&&k!=1)||(i!=1&&j==1&&k!=1)||(i!=1&&j!=1&&k==1)){
						blocks[i][j][k].setType(BlockType.TWOFACES);
					}else if((i==1&&j==1&&k!=1)||(i==1&&j!=1&&k==1)||(i!=1&&j==1&&k==1)){
						blocks[i][j][k].setType(BlockType.ONEFACE);
					}else{
						blocks[i][j][k].setType(BlockType.ZEROFACE);
					}
					Aspect[] aspectstt=new Aspect[3];
					if(i==0){
						aspectstt[0]=new Aspect("white",Pole.DOWN);
					}else if(i==2){
						aspectstt[0]=new Aspect("yellow",Pole.UP);
					}
					if(j==0){
						if(aspectstt[0]==null){
							aspectstt[0]=new Aspect("orange",Pole.BACK);
						}else{
							aspectstt[1]=new Aspect("orange",Pole.BACK);
						}
					}else if(j==2){
						if(aspectstt[0]==null){
							aspectstt[0]=new Aspect("red",Pole.FRONT);
						}else{
							aspectstt[1]=new Aspect("red",Pole.FRONT);
						}
					}
					if(k==0){
						if(aspectstt[0]==null){
							aspectstt[0]=new Aspect("blue",Pole.LEFT);
						}else if(aspectstt[1]==null){
							aspectstt[1]=new Aspect("blue",Pole.LEFT);
						}else{
							aspectstt[2]=new Aspect("blue",Pole.LEFT);
						}
					}else if(k==2){
						if(aspectstt[0]==null){
							aspectstt[0]=new Aspect("green",Pole.RIGHT);
						}else if(aspectstt[1]==null){
							aspectstt[1]=new Aspect("green",Pole.RIGHT);
						}else{
							aspectstt[2]=new Aspect("green",Pole.RIGHT);
						}
					}
					Aspect[] aspects=new Aspect[0];
					if(blocks[i][j][k].getType()==BlockType.ONEFACE){
						aspects=Arrays.copyOf(aspectstt, 1);
					}else if(blocks[i][j][k].getType()==BlockType.TWOFACES){
						aspects=Arrays.copyOf(aspectstt, 2);
					}else if(blocks[i][j][k].getType()==BlockType.THREEFACES){
						aspects=Arrays.copyOf(aspectstt, 3);
					}
					blocks[i][j][k].setAspects(aspects);
				}
			}
		}
		return blocks;
	}
	/*
	 * 获取一些块的某个朝向上的颜色(null代表是没有此朝向上的面,"-"代表块的面信息数组为null)
	 */
	public static String[] thePoleColorsOfSomeBlocks(Block[] someBlocks,Pole pole){
		if(someBlocks==null){
			return null;
		}
		String[] colors=new String[someBlocks.length];
		for(int i=0;i<someBlocks.length;i++){
			Aspect[] aspects = someBlocks[i].getAspects();
			if(aspects==null){
				colors[i]="-";
			}
			for(int j=0;j<aspects.length;j++){
				if(aspects[j].getPole()==pole){
					colors[i]=aspects[j].getColor();
				}
			}
		}
		return colors;
	}
	
	/*
	 * 获取一个面的所有块的颜色（顺序为模块组顺序）
	 */
	public static String[] onePoleColors(Block[][][] blocks,Pole pole) throws Exception{
		if(blocks==null){
			throw new Exception("blocks is null!");
		}
		if(blocks.length==0){
			throw new Exception("the blocks is not a real Cube!");
		}
		int num=blocks.length;
		for(int i=0;i<num;i++){
			if(blocks[i]==null){
				throw new Exception("the blocks is not a real Cube!");
			}
			if(blocks[i].length!=num){
				throw new Exception("the blocks is not a real Cube!");
			}
			for(int j=0;j<num;j++){
				if(blocks[i][j]==null){
					throw new Exception("the blocks is not a real Cube!");
				}
				if(blocks[i][j].length!=num){
					throw new Exception("the blocks is not a real Cube!");
				}
				for(int k=0;k<num;k++){
					if(blocks[i][j][k]==null){
						throw new Exception("the blocks is not a real Cube!");
					}					
				}
			}
		}
		String[] colors=new String[num*num];
		Block[] pblocks=new Block[num*num]; 
		for(int i=0;i<num;i++){
			for(int j=0;j<num;j++){
				switch(pole){
				case FRONT:	
					pblocks[i*num+j]=blocks[i][num-1][j];
					break;
				case BACK:
					pblocks[i*num+j]=blocks[i][0][j];
					break;
				case UP:
					pblocks[i*num+j]=blocks[num-1][i][j];
					break;
				case DOWN:
					pblocks[i*num+j]=blocks[0][i][j];
					break;
				case LEFT:
					pblocks[i*num+j]=blocks[i][j][0];
					break;
				case RIGHT:
					pblocks[i*num+j]=blocks[i][j][num-1];
					break;
				}	
			}
		}
		colors=thePoleColorsOfSomeBlocks(pblocks,pole);
		return colors;
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		MagicCube magicCube=new MagicCube();
		Block[][][] blocks=magicCube.initBlocks3ForSample();
		Block[][][] cloneBlocks=MagicCube.deepClone(blocks);
		System.out.println(MagicCube.blocksToJsonString(blocks));
		magicCube.setBlocks(blocks);
		magicCube.operateCubeForOrdinaryIndex(Operation.FRONTCLOCKWISE, 1);
		magicCube.operateCubeForOrdinaryIndex(Operation.UPCLOCKWISE, 1);
		magicCube.operateCubeForOrdinaryIndex(Operation.ANTIUPCLOCKWISE, 1);
		magicCube.operateCubeForOrdinaryIndex(Operation.ANTIFRONTCLOCKWISE, 1);
		System.out.println(MagicCube.blocksToJsonString(magicCube.getBlocks()));
		System.out.println(MagicCube.judgeBocksEqual(cloneBlocks, magicCube.getBlocks()));
		
		String[] poleColors = MagicCube.onePoleColors(magicCube.getBlocks(),Pole.FRONT);
		for(int i=0;i<poleColors.length;i++){
			System.out.print(poleColors[i]+" ");
		}
		List<Map<Operation,Integer>[]> list=MagicCube.makeOperationList(6, 3, 1);
		for(Map<Operation,Integer>[] maps:list){
			for(Map<Operation,Integer> map:maps){
				System.out.print(map+" ");
			}
			System.out.println();
		}
		
	}
	
	
	
	/*
	 * 构造操作序列,参数分别为步数,维数,偏好(只操作某一层,例如：对于三阶,只需操作外层,值为1；如果为0，没有限制)
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<Operation,Integer>[]> makeOperationList(int stepnum,int dimensions,int favor){
		Operation[] operations=new Operation[]{Operation.FRONTCLOCKWISE,Operation.BACKCLOCKWISE,Operation.UPCLOCKWISE,Operation.DOWNCLOCKWISE,Operation.LEFTCLOCKWISE,Operation.RIGHTCLOCKWISE,
				Operation.ANTIFRONTCLOCKWISE,Operation.ANTIBACKCLOCKWISE,Operation.ANTIUPCLOCKWISE,Operation.ANTIDOWNCLOCKWISE,Operation.ANTILEFTCLOCKWISE,Operation.ANTIRIGHTCLOCKWISE};
		List<Map<Operation,Integer>[]> list=new ArrayList<Map<Operation,Integer>[]>();
		int opn=operations.length;
		Map<Operation,Integer>[] mp=null;//操作参数数组
		if(favor==0){
			int dn=(dimensions+1)/2;
			mp=new HashMap[opn*dn];
			for(int m=0;m<opn;m++){
				for(int n=0;n<dn;n++){
					mp[m*dn+n]=new HashMap<Operation,Integer>();
					mp[m*dn+n].put(operations[m], n+1);
				}
			}
		}else{
			mp=new HashMap[opn];
			for(int m=0;m<opn;m++){
				mp[m]=new HashMap<Operation,Integer>();
				mp[m].put(operations[m], favor);
			}
		}
		int[] meter=new int[stepnum];//记录数组，每个元素最大值为mp.length-1
		while(meter[stepnum-1]<mp.length){//电表算法
			Map<Operation,Integer>[] map=new HashMap[stepnum];
			for(int i=0;i<meter.length;i++){
				map[i]=mp[meter[i]];
			}
			for(int i=0;i<meter.length;i++){
				if(i==0){
					meter[0]++;
				}else{
					if(meter[i-1]==mp.length){
						meter[i-1]=0;
						meter[i]++;
					}
				}
			}
			list.add(map);
		}
		return list;
	}
	
}
