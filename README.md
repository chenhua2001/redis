### 创建一个CHMap实现数据存，取，扩容，删除
#### 取
    对key进行hash
    遍历该位置上链表
        如果key.equals(链表上面的key) 返回答案
    返回null
#### 存
    对key进行hash
    获取链表
    遍历链表看看是否有key相同（equals）的元素
            有则赋值到e退出
    如果e不为空，则赋值
    如果e为空，则新建，并且放入该链表
        size++;
#### 扩容
    当size>阈值的时候，我们就要进行扩容（这里还有一个ArrayList会自动扩容的问题）
    扩容的大小是ArrayList的容量的两倍
    rehash（）
#### 删除
    对key进行hash
    获取链表
    链表头是否为key，是就删除头结点
    遍历链表
        如果key.next跟key equals
        将对应的节点进行删除
        size--;
#### 缩容
    如果size已经小于容量*最小加载量这个时候就需要缩容
    缩容的大小是大于size的最小2次幂
    rehash（）
#### 思考
    ArrayList本来就有扩容机制
        当list中的size到达capacity的时候就会进行扩容，扩容的大小是增加当前的0.5
        但是由于我们的factor一定<=1,并且在put方法的最开始我们就会先判断是否扩容，所以不会等到ArrayList来进行扩容
### 渐进式hash
    将rehash操作平均到每次对table的操作中
#### 如果当前正在处于rehash的过程（用一个标志来证明正在rehash，还有rehash结束）
##### 存
    对一个list进行rehash
    每次放元素当然是放到新的table中
##### 取
    对一个list进行rehash
    每次查元素先到旧的table查
        成功即返回
    再到新的table查
##### 删除
    对一个list进行rehash
    到旧的删
        成功即返回
    到新的删

    