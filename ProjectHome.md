通过wifi让Android控制PC(很早以前就想做这个东西了)<br><br>

这个项目是从<a href='https://code.google.com/p/remotedroid/'>Remotedroid</a>开源项目开始的,但是该项目从2010年就没有更新，功能上有一些缺点<br>
自己对Android部分做了大部分功能的改写，项目底层的改写是后来只保留udp通信部分,以模拟消息队列的方法改进通信方式<br><br><br>
自定义可以任意拖动的键盘布局，可以多个同时触发，而且用户可以自定义未任意按键<br><br><br>
触控板太单调导入一个opengl地球模型<br><br><br>
系统自带键盘和原先读取隐藏edittextview的方法可能会和系统软件(例如输入法)有冲突，所以自定义了键盘控件，自写键盘值实现任意按键ps:android键盘无法实现多个同时点击，暂时用的是stick按键的方法(<a href='https://code.google.com/p/wjw-remote-pc-android/source/browse/#svn%2Ftrunk%2FRemote.GetKeyCodes'>此处使用了直接映射键码的方式</a>)<br><br><br>
打算使用开源项目<a href='http://zinnia.sourceforge.net/'>zinnia</a>作为中文手写输入法，改项目为c++项目，要使用JNI移植到Android上，写好makefile与jni接口编译，已经导入项目，但是使用效果并不理想，纠结<br><br><br>

手写部分的汉字已经完成,之前一直有乱码的情况，考虑到跨平台的原因，最后都统一转为java通用的unicode码进行传输\(<sup>o</sup>)/<br><br><br>

目前做到<br><br>
通信<br>
鼠标<br>
自定义键盘<br>
自定义布局键盘<br>
手写输入汉字在pc端显示<br>