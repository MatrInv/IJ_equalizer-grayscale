// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 						// pour classe Rectangle
import java.lang.Math;					// pour la racine

// Nom de la classe = nom du fichier.  Implémente l'interface PlugInFilter
public class Evaluate_contrast implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}

	public void run(ImageProcessor ip) {
		Rectangle r = ip.getRoi(); // Région d'intérêt sélectionnée (r.x=r.y=0 si aucune)

		if(ip.isGrayscale()){

			double sum=0, Moy=0;

			//calcul de la moyenne d'intensité
			for (int ym=r.y; ym<(r.y+r.height); ym++)
				for (int xm=r.x; xm<(r.x+r.width); xm++)
					Moy+=ip.get(xm,ym);
			
			Moy=Moy/(r.height*r.width);


			//calcul du contraste
			for (int y=r.y; y<(r.y+r.height); y++)
				for (int x=r.x; x<(r.x+r.width); x++)
					sum+=((ip.get(x,y) - Moy) * (ip.get(x,y) - Moy)) ;

			double res = Math.sqrt( ( ((double)1) / (double)(r.height*r.width) ) * sum);

			IJ.log("Contrast :" + res);

		}
		else{
			IJ.log("GrayScale-only needed !");
		}

	}

}
