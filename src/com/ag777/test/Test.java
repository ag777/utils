package com.ag777.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ag777.util.file.FileUtils;
import com.ag777.util.file.IniHelper;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.MapUtils;
import com.ag777.util.security.MD5Utils;

public class Test {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
//		String result = RegexUtils.find("wtmsb", "^([^=]+)=([^=]+)$", "$1=$2");
//		System.out.println(result);
//		Console.log("a=".split("="));
//		IniUtils iu = new IniUtils("e:\\converter_config.ini");
//		.section("bean").value("path.file.out"));
//		System.out.println(iu.getValue("bean", "path.file.out").get());
//		String line = "path.template.in=F:\\temp\\临时";
//		System.out.println(find(line, Pattern.compile("^([^=]+)=([^=]*)$"), "$1=$2"));
//		String a = "\\";
//		String b = "$1";
//		b = b.replaceAll("\\$1", a);
//		System.out.println(b);
//		Map<String, Object> map = MapUtils.of("a", 1);
//		map.remove("b");
//		String a = "\t\t\t";
//		System.out.println(a.replaceAll("(\\t*)\\t", "$1"));
//		String a = "   \r\n a\r\nb\r\n\r\n";
//		System.out.println(a.replaceAll("^\\s+?\\r?\\n", "").replaceAll("\\s+?\\r?\\n$", ""));
		
//		Console.log("a".split("\\.",2));
//		String a = "a\r\nbb\nc\rs";
//		Console.log(StringUtils.toLineList(a));
		String md5 = FileUtils.md5("F:\\temp\\漏扫V3\\wrvas_auth.dat");
		System.out.println(md5);
		System.out.println(MD5Utils.getMD5String("b3706fd762628352430359c9f684f1d8e3ac19bcccdf62619a7a07c47544b12969c59088b87516757cb8140f48856dbc5663b44a5dd6a41033ce840cc4223e50fac08402a947bb0215ebdc6de08879145e5a79680246292d0f214846ef96d2e93a1d090edfed7092cadebaa4b97bcefa39375fbbf83a5ea43d5b252f6216795dfde7e69c4abcdf75fd93244d8f984e430e42ae356a957b7faa4acee7a5745ee3beae15794546b05eac238dd68802d6ff15297f90ec2b0b729f044a85258dc5702df4ff56e74b82e73a77dc83d60368bc5374245d386bf9351ce4bfefcfd125f423c2f50dd7ede8de15297f90ec2b0b729f044a85258dc5700b4f5296a45a01005374245d386bf9351ce4bfefcfd125f44c1ab2e582be6422b98663157f7ccdc87c716e1fe2d5ca9f49f82621096fec961e43f6c48a81b2c76341e96cc72f511a93a9c7249b4f6a267e765d7417c43f8e2dead0af8032b57467665def53bf4db8e710cc787abf1bdd74ae2eaf062373a07d2c8b08ad2cfe6757467b601a706fc67ead0313f67aa42b572e62e2ee9f7289ce81aed8ed47d528f2f4243089b88fa19e1cc6543463519dd7efb2740c998faacfdd8e8ad302833cd7efb2740c998faaf64be1f262b9d23697ac3bd68949a25ccccbd0b41c4669b715297f90ec2b0b722817efd26a438a6facdf4da0bd5c076ae43c2ff2dcc1563379a262d6b8e62d765374245d386bf935dcb5585d418b02756c0dbec777d43df7c81e14b2df7b2b669604266d4827a35a06f214e21c4b7819680b443f97ba0a6ca1fade1c4360c221badb8a25aba62d775ea34522830ed5ccaf286bd5743d3eea0c65f2c9080b3f5a2c4429d2f981e7a53ea4c930765e143447a96dfeaeb3adfa0a9c663b5cd0262d43bd9138fc57268f6c0dbec777d43df75fa65ed5ad58feea6d8ec8d92ca4a2f80345ab108fc007019fda192e7e966c5d2939188b455753e6c6e7007dfdb4d57123c2f50dd7ede8de15297f90ec2b0b729f044a85258dc5700b4f5296a45a01005374245d386bf9351ce4bfefcfd125f489d1d2dd0078f1b6e611456791abc081ba591c105feabf08b1f58c7a9bf2a9b8845f3a16331217799022b8f1cd7abadcdd60d4ffef8971f3d466d394b8990e84a25fdc92ee40d6eb6c0dbec777d43df7c81e14b2df7b2b669604266d4827a35a0c65f2c9080b3f5ad58cc7152720b552ba591c105feabf08b1f58c7a9bf2a9b8c81e14b2df7b2b669604266d4827a35a06f214e21c4b7819680b443f97ba0a6c0b4f5296a45a01005374245d386bf9351ce4bfefcfd125f489d1d2dd0078f1b6d13b09f17999728d1f06b32c3f9b0f3628a7a478b57366bf65252302d5d0869aa58efe2a4152ad99e8ea9e059164a1b1ea12948827f5f6553489407ac5143e9be9e1c6356a5638ac69ead8972407772cced6a06b41775b6b237282521a7b2a742ccf9a6fe057b8a3f0e4b79ff680f65bfc0ebc5b605612c1dab6bc2408cc93b162f510f8148bc4775d63ac6a51e7d3040b64f2f847f000d6f091f13470930423fb5de47aafbc3b83"));
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回(借鉴某爬虫app的github开源代码，这是真心好用)
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String find(String src, Pattern pattern, String replacement) {
		if(src != null && pattern != null) {
			Matcher matcher = pattern.matcher(src);

			if (!matcher.find()) {	//没有匹配到则返回null
				return null;
			} else {
				return getReplacement(matcher, replacement);
			}

		} else {	//如果元字符串为null或者正则表达式为null，返回源字符串
			return src;
		}
	}
	
	private static String getReplacement(Matcher matcher, String replacement) {
		String temp = replacement;
		if (replacement != null) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String replace = matcher.group(i);
				temp =  temp.replace("$" + i, (replace != null) ? replace : "");
			}
			return temp;
		} else {
			return matcher.group(0);
		}
	}
}
