// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 						// pour classe Rectangle

// Nom de la classe = nom du fichier.  Implémente l'interface PlugInFilter
public class Equalize_grayscale implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		// Accepte tous types d'images, piles d'images et RoIs, même non rectangulaires
		return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
	}

	public void run(ImageProcessor ip) {
		Rectangle r = ip.getRoi(); // Région d'intérêt sélectionnée (r.x=r.y=0 si aucune)

		int distrib = 256;

		//on crée un tableau de taille 256 contenant qui à un niveau de gris associe le nombre de pixels correspondant
		int histo[] = new int[distrib];

		//on parcours tous les pixels de r en incrémentant la valeur indexée par son niveau de gris
		//on obtient ainsi l'histogramme de r
		for (int y=r.y; y<(r.y+r.height); y++)
			for (int x=r.x; x<(r.x+r.width); x++)
				histo[(int)ip.get(x,y)]++;


		//pour chaque niveau de gris, on évalue la proportion de r dont la valeur est inférieure ou égale à n

		double R[] = new double[distrib];
		int p = 0;

		for(int i=0;i<distrib;i++){

			p+=histo[i];

			R[i] = ((double)p)/(double)(r.width*r.height);

		}
		

		//calcul de la transformation
		for (int yt=r.y; yt<(r.y+r.height); yt++)
			for (int xt=r.x; xt<(r.x+r.width); xt++)
				ip.set(xt,yt,(int)(R[ip.get(xt,yt)]*(distrib-1)));

	}

}
