export const columns1 = [
    {
        key: 'menu_id',
        title: '菜单ID',
        width: 80,
        fixed : 'left',
        sortable: true
    },
    {
        key: 'parent_id',
        title: '父菜单ID',
        editable: true,
        width: 80
    },
    {
        key: 'name',
        title: '菜单名称',
        editable: true,
        width: 180
    },
    {
        key: 'type',
        title: '菜单类型',
        editable: false,
        width: 80
    },
    {
        key: 'perms',
        title: '菜单权限',
        editable: true ,
        width: 180
    },
    {
        key: 'url',
        title: '菜单资源',
        editable: true,
        width: 180
    },
    {
        key: 'gmt_create',
        title: '创建日期',
        width: 150
    },
    {
        key: 'gmt_modified',
        title: '最近修改',
        width: 150
    },
    {
        title: '操作',
        align: 'center',
        fixed: 'right',
        width: 250,
        key: 'handle',
        handle: ['edit', 'delete','diliver']
    }
];



export const searchTable2 = [
    {
        menu_id: '1',
        parent_id: '0',
        name: '菜单1',
        perm: '',
        url: '/main/me',
        gmt_create:'2017-12-01 22:20'

    },
    {
        menu_id: '2',
        parent_id: '0',
        name: '菜单2',
        perm: 'sys:main:menu',
        url: '/main/menu3',
        gmt_create:'2018-12-01 22:20'
    },
    {
        menu_id: '3',
        parent_id: '1',
        name: '菜单3',
        perm: 'sys:main:menu4',
        url: '/main/menu4',
        gmt_create:'2017-1-01 22:20'
    },
    {
        menu_id: '6',
        parent_id: '2',
        name: '菜单6',
        perm: 'sys:main:nu',
        url: '/main/menu/4',
        gmt_create:'2027-12-01 22:20'
    },
    
];
