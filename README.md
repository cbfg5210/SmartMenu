
##base on [xue5455-SmartMenu](https://github.com/xue5455/SmartMenu)
 
 
###Screenshot
![Screenshot](https://raw.githubusercontent.com/xue5455/SmartMenu/master/screenshot/Gif.gif)

###How To Use
```
    <declare-styleable name="SmartMenu">
            <attr name="innerPadding" format="dimension"/>//菜单项距离
            <attr name="outerPadding" format="dimension"/>//菜单左右边界距离
            <attr name="smartBtnSize" format="dimension"/>//中间按钮的尺寸
            <attr name="verticalPadding" format="dimension"/>//菜单的上边界和中间按钮的上边界的距离
            <attr name="bgColor" format="color|reference"/>//背景色
            <attr name="shadowColor" format="color|reference"/>//阴影颜色
            <attr name="smartViewRes" format="reference"/>//自定义的中间按钮布局
            <!--smart button content:def-->
            <attr name="dotRadius" format="dimension"/>//中间按钮的小圆点的半径
            <attr name="dotDistance" format="dimension"/>//左边的小圆点和右边的小圆点的距离(包括左右圆点的直径)
            <attr name="dotColor" format="color|reference"/>//圆点的颜色
            <!--smart button content:image-->
            <attr name="android:src"/>//设置中间按钮的内容为图片
            <attr name="imageRatio" format="float"/>//图片比例
            <!--smart button content:text-->
            <attr name="android:text"/>//设置中间按钮的内容为文字
            <attr name="android:textSize"/>//文字大小
            <attr name="android:textColor"/>//文字颜色
        </declare-styleable>
```

使用方式：

一、默认中间按钮，菜单项使用图片：
```
smartMenu.setImages(
                new int[]{
                        R.mipmap.icon_album,
                        R.mipmap.icon_comment,
                        R.mipmap.icon_comment,
                        R.mipmap.icon_comment,
                        R.mipmap.icon_draft,
                        R.mipmap.icon_like
                }
                , (AdapterView<?> parent, View view, int position, long id) -> {
                    toast("item" + position);
                });
```

二、默认中间按钮，菜单项使用文字：
```
smartMenu.setTexts(new String[]{
                "A",
                "B",
                "C",
                "D",
                "E",
                "F",
        }, (AdapterView<?> parent, View view, int position, long id) -> {
            toast("item" + position);
        });
```

三、自定义菜单项：
```
smartMenu.setAdapter(menuAdapter);

public class MenuAdapter extends BaseAdapter implements View.OnClickListener{

    private int[] images = new int[]{R.mipmap.icon_album,
            R.mipmap.icon_comment,
            R.mipmap.icon_draft,
            R.mipmap.icon_like};
    private ItemEventListener listener;

    public void setListener(ItemEventListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
        view.setOnClickListener(this);
        view.setTag(i);
        ImageView img = (ImageView) view.findViewById(R.id.image_view);
        img.setImageResource(images[i]);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onEventNotify(view,(int)view.getTag());
        }
    }

}
```

四：自定义中间按钮：<br>
1、设置中间按钮为图片：
```
android:src="@mipmap/icon_album"
//app:imageRatio="0.4"
```

2、设置中间按钮为文字：
```
android:text="GO"
android:textSize="18sp"
android:textColor="@color/white"
```

3、自定义中间按钮:
```
app:smartViewRes="@layout/layout_smart"
```

存在的问题：<br>
1、菜单项为文字时的点击效果待优化；<br>
2、菜单项为文字时长度差距会影响收缩效果；
