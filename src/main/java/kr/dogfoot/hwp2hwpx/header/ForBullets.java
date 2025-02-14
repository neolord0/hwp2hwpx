package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.Bullet;

import java.util.ArrayList;

public class ForBullets extends Converter {
    private Bullet bullet;
    private kr.dogfoot.hwplib.object.docinfo.Bullet hwpBullet;

    public ForBullets(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<kr.dogfoot.hwplib.object.docinfo.Bullet> bulletList) {
        if (bulletList.size() == 0) return;

        refList.createBullets();

        int id = 1;
        for (kr.dogfoot.hwplib.object.docinfo.Bullet item : bulletList) {
            bullet = refList.bullets().addNew().idAnd(String.valueOf(id));
            hwpBullet = item;

            bullet();
        }
    }

    private void bullet() {
        bullet
                ._charAnd(ValueConvertor.stringNullCheck(hwpBullet.getBulletChar().toUTF16LEString()))
                .checkedCharAnd(ValueConvertor.stringNullCheck(hwpBullet.getCheckBulletChar().toUTF16LEString()))
                .useImage(hwpBullet.getImageBullet());

        if (hwpBullet.getImageBullet()) {
            bullet.createImg();
            ForFillBrush.image(bullet.img(), hwpBullet.getImageBulletInfo(), parameter);
        }

        bullet.createParaHead();
        bullet.paraHead().level((byte) 0);
        ForNumberings.paraHead(bullet.paraHead(), hwpBullet.getParagraphHeadInfo());
    }
}
