import nzh from 'nzh/cn';
import { Notify } from 'quasar';
import router from '@/router';
import VueRouter from 'vue-router'

export function fixedZero(val) {
    return val * 1 < 10 ? `0${val}` : val;
}



export function digitUppercase(n) {
    return nzh.toMoney(n);
}

export function formatWan(val) {
    const v = val * 1;
    if (!v || Number.isNaN(v)) return '';

    let result = val;
    if (val > 10000) {
        result = Math.floor(val / 10000);
        result = `${result}万`;
    }
    return result;
}

export function formatDicDisplay(options, option) {
    let display = ''
    if (null === option) {
        return display;
    }
    options.forEach(op => {
        if (option.constructor === Object) {
            option = option.value
        }
        if (op.value === option) {
            display = op.label
        }
    })
    return display
}

export function positiveNotify(message) {
    let setting = {
        type: 'positive',
        position: 'top'
    };
    creteNotify(setting, message);
}
export function negativeNotify(message) {
    let setting = {
        type: 'negative',
        position: 'top'
    };
    creteNotify(setting, message);
}
export function warningNotify(message) {
    let setting = {
        type: 'warning',
        position: 'top'
    };
    creteNotify(setting, message);
}
export function infoNotify(message) {
    let setting = {
        type: 'info',
        position: 'top'
    };
    creteNotify(setting, message);
}
export function ongoingNotify(message) {
    let setting = {
        type: 'ongoing',
        position: 'top'
    };
    creteNotify(setting, message);
}
function creteNotify(setting, message) {
    if (typeof message === 'string') {
        setting.message = message;
    } else {
        Object.assign(setting, message);
    }
    Notify.create(setting);
}


export function addRouters(data) {
    let routers = []
    generateRouter(routers, eval(data))
    resetRouter()
    router.addRoutes(routers)
}

// 重置路由
function resetRouter() {
    const routes = router.options.routes  //默认静态路由
    const newRouter = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    })
    router.matcher = newRouter.matcher // reset router
}

function generateRouter(routers, data) {
    data.forEach(item => {
        let r = Object.assign({}, item)
        r.component = importComponent(r.component)
        if (item.children) {
            r.children = []
            generateRouter(r.children, item.children)
        }
        routers.push(r)
    })
}
function importComponent(name) {
    name = name.toString()
    let a = name.split('@')
    // console.log('a:' + a)
    let b = a[1].substring(1, a[1].length - 2)
    // console.log('b:' + b)
    return () => import(`@/${b}`)
}

export function getMenuList(data) {
    let routers = []
    if (null === data || data.length === 0) {
        return routers
    }
    routers = eval(data)
    routers = routers
        .filter((menu) => menu.path === '/provider')
        .map((menu) => {
            return menu.children
        })
    return routers[0]
}

export function save2Json(data, fileName) {
    if (!data) {
        console.log('data is null');
        return;
    }
    if (!fileName)
        fileName = 'data.json'
    if (typeof data === 'object') {
        data = JSON.stringify(data, undefined, 4)
    }
    var blob = new Blob([data], { type: 'text/json' }),
        e = document.createEvent('MouseEvents'),
        a = document.createElement('a')
    a.download = fileName
    a.href = window.URL.createObjectURL(blob)
    a.dataset.downloadurl = ['text/json', a.download, a.href].join(':')
    e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
    a.dispatchEvent(e)
}

import { jsPDF } from 'jspdf'
import html2canvas from 'html2canvas'

export function html2pdf(html, isOne, pdfName) {
    let contentWidth = html.clientWidth // 获得宽
    let contentHeight = html.clientHeight // 获得高

    return html2canvas(html).then(canvas => {
        let pageData = canvas.toDataURL('image/jpeg', 1) // 清晰度 0 - 1
        let pdf
        if (isOne) {
            // 单页
            // jspdf.js 插件对单页面的最大宽高限制 为 14400
            let limit = 14400

            if (contentHeight > limit) {
                let contentScale = limit / contentHeight
                contentHeight = limit
                contentWidth = contentScale * contentWidth
            }

            let orientation = 'p'
            // 在 jspdf 源码里，如果是 orientation = 'p' 且 width > height 时， 会把 width 和 height 值交换，
            // 类似于 把 orientation 的值修改为 'l' , 反之亦同。
            if (contentWidth > contentHeight) {
                orientation = 'l'
            }

            // orientation Possible values are "portrait" or "landscape" (or shortcuts "p" or "l")
            pdf = new jsPDF({
                orientation: orientation,
                unit: 'pt',
                format: [contentWidth, contentHeight]
            }) // 下载尺寸 a4 纸 比例

            // pdf.addImage(pageData, 'JPEG', 左，上，宽度，高度)设置
            pdf.addImage(pageData, 'JPEG', 0, 0, contentWidth, contentHeight)

        } else {
            //一页 pdf 显示 html 页面生成的 canvas高度
            let pageHeight = (contentWidth / 552.28) * 841.89
            //未生成 pdf 的 html页面高度
            let leftHeight = contentHeight
            //页面偏移
            let position = 0
            //a4纸的尺寸[595.28,841.89]，html 页面生成的 canvas 在pdf中图片的宽高
            let imgWidth = 555.28
            let imgHeight = (imgWidth / contentWidth) * contentHeight

            pdf = new jsPDF({
                orientation: '',
                unit: 'pt',
                format: 'a4'
            })// 下载尺寸 a4 纸 比例
            //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
            //当内容未超过pdf一页显示的范围，无需分页
            if (leftHeight < pageHeight) {
                pdf.addImage(pageData, 'JPEG', 20, 0, imgWidth, imgHeight)
            } else {
                while (leftHeight > 0) {
                    pdf.addImage(pageData, 'JPEG', 20, position, imgWidth, imgHeight)
                    leftHeight -= pageHeight
                    position -= 841.89
                    //避免添加空白页
                    if (leftHeight > 0) {
                        pdf.addPage()
                    }
                }
            }
        }
        return pdf.save(pdfName)
    });
}