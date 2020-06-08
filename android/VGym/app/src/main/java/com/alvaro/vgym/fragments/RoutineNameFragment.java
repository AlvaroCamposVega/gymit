package com.alvaro.vgym.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alvaro.vgym.R;
import com.alvaro.vgym.model.Routine;
import com.alvaro.vgym.model.Workout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineNameFragment extends Fragment
{
    public static final String TAG = "newRoutineName";
    private static final String ARG_PARAM = "routine";

    private Routine routine;
    private MaterialToolbar topAppBar;

    private TextInputEditText nameInput;
    // Required empty public constructor
    public RoutineNameFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewRoutineName.
     */
    public static RoutineNameFragment newInstance(Routine routine)
    {
        RoutineNameFragment fragment = new RoutineNameFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, routine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            routine = (Routine) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(
            R.layout.fragment_routine_name,
            container,
            false
        );

        topAppBar = view.findViewById(R.id.fragmentTopAppBar);
        setTopAppBarListener();

        nameInput = view.findViewById(R.id.routineNameName);
        nameInput.setText(routine.getName());

        return view;
    }

    /**
     * Establece el listener de la top app bar para este fragmento.
     */
    private void setTopAppBarListener()
    {   // Obtenemos el fragmento del diálogo de nueva rutina
        RoutineDialogFragment routineDialogFragment = (RoutineDialogFragment)
            getParentFragment();
        // Listener del botón de cancelar (la X)
        topAppBar.setNavigationOnClickListener(v -> {
            /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            WorkoutFragment workoutFragment = WorkoutFragment.newInstance();
            BottomNavigationFragment bottomNavigation = BottomNavigationFragment.newInstance();

            fragmentManager.beginTransaction()
                .replace(R.id.mainWorkoutFragment, workoutFragment, WorkoutFragment.TAG)
                .replace(R.id.mainBottomNavigation, bottomNavigation, BottomNavigationFragment.TAG)
                .commit();*/

            routineDialogFragment.dismiss();
        });
        // Listener del texto SIGUIENTE
        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.mnuRoutineNameNext)
            {   // Si no hay errores en el nombre de la rutina
                if (setRoutineNameFromTextField())
                {   // Reemplazamos el contenido por el fragmento que controla los días de la rutina
                    FragmentManager childFragmentManager = routineDialogFragment
                        .getChildFragmentManager();

                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_left_in,
                            R.anim.fragment_fade_out,
                            R.anim.slide_right_in,
                            R.anim.fragment_fade_out
                        )
                        .replace(
                            R.id.fragmentRoutineDialogContainer,
                            WorkoutListFragment.newInstance(routine),
                            WorkoutListFragment.TAG
                        )
                        .addToBackStack(null)
                        .commit();
                } // Si hay errores en el nombre mandamos una alerta al usuario
                else
                {
                    Snackbar.make(topAppBar, R.string.fields_empty, Snackbar.LENGTH_SHORT).show();
                }
            }

            return false;
        });
    }

    /**
     * Establece el nombre de la rutina desde el campo nombre.
     *
     * @return False si el campo está vacío.
     */
    public boolean setRoutineNameFromTextField()
    {
        boolean result = false;
        String name = nameInput.getText().toString();
        // Si el campo del nombre de la rutina no está vacío establecemos el nombre en el objeto
        // de la rutina y cambiamos el resultado a verdadero
        if (!name.trim().isEmpty()) { routine.setName(name); result = true; }

        return result;
    }
}
