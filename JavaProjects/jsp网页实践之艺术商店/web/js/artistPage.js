/**
 * Created by Bing Chen on 2017/7/23.
 */
$(document).ready(function () {
    //创建两个数组，存储作家信息与页码按钮
    arrayArtist = new Array();
    arrayButton = new Array();
    $.get("./artistPageServlet", function (data, status) {
        //获取用户信息并存储到数组
        var List = JSON.parse(data);
        List.forEach(function (artist) {
            var arrayElement = new Array();
            arrayElement[0] = artist.artistId;
            if (artist.firstName === null) {
                arrayElement[1] = "";
            } else {
                arrayElement[1] = artist.firstName;
            }
            if (artist.lastName === null) {
                arrayElement[2] = "";
            } else {
                arrayElement[2] = artist.lastName;
            }
            arrayElement[3] = artist.artistLink;
            arrayArtist.push(arrayElement);
        });
        //判断总共页数
        var pageTotal = parseInt((arrayArtist.length % 12 === 0) ? (arrayArtist.length / 12) : (arrayArtist.length / 12 + 1));
        //获取所有页码按钮并初始化按钮样式
        for (var m = 0; m < pageTotal; m++) {
            var string = "artistChangePage" + m;
            arrayButton[m] = document.getElementById(string);
            arrayButton[m].style.backgroundColor = "#fff";
            arrayButton[m].style.color = "#337ab7";
        }
        arrayButton[0].style.backgroundColor = "#ABABAB";
        arrayButton[0].style.color = "blue";
        //为按钮设置事件
        for (var i = 0; i < pageTotal; i++) {
            arrayButton[i].onclick = function (numbertest) {
                return function () {
                    //统一改变所有按钮样式
                    for (var m = 0; m < pageTotal; m++) {
                        arrayButton[m].style.backgroundColor = "#fff";
                        arrayButton[m].style.color = "#337ab7";
                    }
                    //为当前按钮设置特殊样式
                    arrayButton[numbertest].style.backgroundColor = "#ABABAB";
                    arrayButton[numbertest].style.color = "blue";
                    //判断当前按钮加一是否小于总页码
                    if ((numbertest + 1) < pageTotal) {
                        //小于总页码时，改变当前页面的链接、图片的节点src或者href
                        for (var j = 0; j < 12; j++) {
                            var artistToDetailLink = document.getElementById("artistToDetailLink" + j);
                            var SecondArtistToDetailLink = document.getElementById("SecondArtistToDetailLink" + j);
                            var artistImg = document.getElementById("artistImg" + j);
                            var artistName = document.getElementById("artistName" + j);
                            var artistLink = document.getElementById("artistLink" + j);
                            artistToDetailLink.setAttribute("href", "http://localhost:8080/artistDetail?id=" + arrayArtist[numbertest * 12 + j][0]);
                            SecondArtistToDetailLink.setAttribute("href", "http://localhost:8080/artistDetail?id=" + arrayArtist[numbertest * 12 + j][0]);
                            artistImg.setAttribute("src","art-images/artists/medium/"+arrayArtist[numbertest * 12 + j][0]+".jpg");
                            artistName.innerHTML=arrayArtist[numbertest * 12 + j][1]+arrayArtist[numbertest * 12 + j][2];
                            artistLink.setAttribute("href",arrayArtist[numbertest * 12 + j][3]);
                            artistLink.innerHTML=arrayArtist[numbertest * 12 + j][3];
                            artistToDetailLink.parentNode.style.visibility = "visible";
                        }
                    }else {
                        //按钮加一不小于总页码时
                        for (var l = 0, k = 0; l < 12; l++, k++) {
                            var artistToDetailLink = document.getElementById("artistToDetailLink" + l);
                            var SecondArtistToDetailLink = document.getElementById("SecondArtistToDetailLink" + l);
                            var artistImg = document.getElementById("artistImg" + l);
                            var artistName = document.getElementById("artistName" + l);
                            var artistLink = document.getElementById("artistLink" + l);
                            //判断下标是否超出数组长度，
                            if (k < arrayArtist.length - numbertest * 12) {
                                //未超出，改变元素的属性
                                artistToDetailLink.setAttribute("href", "http://localhost:8080/artistDetail?id=" + arrayArtist[numbertest * 12 + k][0]);
                                SecondArtistToDetailLink.setAttribute("href", "http://localhost:8080/artistDetail?id=" + arrayArtist[numbertest * 12 + k][0]);
                                artistImg.setAttribute("src","art-images/artists/medium/"+arrayArtist[numbertest * 12 + k][0]+".jpg");
                                artistName.innerHTML=arrayArtist[numbertest * 12 + k][1]+arrayArtist[numbertest * 12 + k][2];
                                artistLink.setAttribute("href",arrayArtist[numbertest * 12 + k][3]);
                                artistLink.innerHTML=arrayArtist[numbertest * 12 + k][3];
                                artistToDetailLink.parentNode.style.visibility = "visible";
                            } else {
                                //下标超出数组长度时，把其父元素设置为不可见
                                artistToDetailLink.parentNode.style.visibility = "hidden";
                            }
                        }
                    }
                }
            }(i)
        }
    });
});