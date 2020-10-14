var IDCardHelper = {
    isIdCard: function (sId) {
        if (isNaN(sId)) return false;
        var day =parseInt(sId.substr(10, 2),10);
        var month = parseInt(sId.substr(8, 2),10);
        var year = parseInt(sId.substr(6, 2), 10);
        var sID_17 ="";
        if (sId.length === 18) {
            sID_17 = sId.substr(0, 17);
            var day = parseInt(sId.substr(12, 2), 10);
            var month = parseInt(sId.substr(10, 2), 10);
            var year = parseInt(sId.substr(6, 4), 10);
        }
        
        if (sId.length === 15) {           
            if (IDCardHelper.verifyDate(day, month, year))
                return true;
            else
                return false;
        }       
        else if (sId.length === 18) {
            if (isNaN(sID_17)) return false;
            if (IDCardHelper.verifyDate(day, month, year)) {
                if (IDCardHelper.CalID_17to18(sID_17) === sId)
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
        return false;
    },
    

    verifyDate: function (day, month, year) {
        if (!day) return false;
        var iToday = new Date();
        month = month ? month - 1 : iToday.getMonth();
        year = year ? IDCardHelper.y2k(year) : iToday.getFullYear();
        var iDate = new Date(year, month, day);
       
        if ((iDate.getFullYear() === year) && (iDate.getMonth() === month) && (iDate.getDate() === day)) {
            return true;
        }
        else {
            return false;
        }
    },
    CalID_17to18: function (sId) {
        var aW = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7);
        var aA = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
        var aP = new Array(17);
        var aB = new Array(17);
        var i, iSum = 0;
        for (i = 1; i < 18; i++)
            aP[i] = sId.substr(17 - i, 1);
        for (i = 1; i < 18; i++) {
            aB[i] = parseInt(aP[i]) * parseInt(aW[i]);
            iSum += aB[i];
        }
        return sId + aA[iSum % 11];
    },
    /*根据15位身份证算出18位身份证*/
    CalID_15to18: function (sId) {
        return IDCardHelper.CalID_17to18(sId.substr(0, 6) + "19" + sId.substr(6));
    },
    y2k: function(iYear){ return (iYear < 1000) ? iYear + 1900 : iYear}
};